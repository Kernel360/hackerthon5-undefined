terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "5.81.0"
    }
  }
}
locals {
  secrets_version = formatdate("YYYYMMDDHHMM", timestamp())
}

// VPC, Subnet, Security Group, internet gateway, route table
resource "aws_vpc" "uptime-main" {
  cidr_block = var.vpc_cidr
  tags = {
    Name = "main-vpc"
  }
}

// 인터넷 게이트웨이 생성
resource "aws_internet_gateway" "uptime-main" {
  vpc_id = aws_vpc.uptime-main.id

  tags = {
    Name = "main-internet-gateway"
  }
}

// 라우트 테이블 생성 및 인터넷 게이트웨이 연결
resource "aws_route_table" "uptime-main" {
  vpc_id = aws_vpc.uptime-main.id

  route {
    cidr_block = "0.0.0.0/0"
    gateway_id = aws_internet_gateway.uptime-main.id
  }

  tags = {
    Name = "main-route-table"
  }
}

// 서브넷과 라우트 테이블 연결
resource "aws_route_table_association" "uptime-main" {
  subnet_id      = aws_subnet.uptime-main.id
  route_table_id = aws_route_table.uptime-main.id
}

resource "aws_subnet" "uptime-main" {
  vpc_id            = aws_vpc.uptime-main.id
  cidr_block        = var.subnet_cidr_main
  availability_zone = "ap-northeast-2a"
  tags = {
    Name = "main-subnet"
  }
}

resource "aws_subnet" "uptime-sub" {
    vpc_id            = aws_vpc.uptime-main.id
    cidr_block        = var.subnet_cidr_sub
    availability_zone = "ap-northeast-2c"
    tags = {
        Name = "sub-subnet"
    }
}

resource "aws_security_group" "uptime-backend_sg" {
  vpc_id = aws_vpc.uptime-main.id

  ingress {
    from_port   = 22
    to_port     = 22
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  ingress {
    from_port   = 80
    to_port     = 80
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  ingress {
    from_port   = 8080
    to_port     = 8080
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  ingress {
    from_port  = 443
    to_port     = 443
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = {
      Name = "uptime-backend-security-group"
  }
}

// RDS
resource "aws_security_group" "uptime-rds_sg" {
  vpc_id = aws_vpc.uptime-main.id

  ingress {
    from_port   = 3306
    to_port     = 3306
    protocol    = "tcp"
    security_groups = [aws_security_group.uptime-backend_sg.id]
  }

  egress {
    from_port = 0
    to_port   = 0
    protocol  = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = {
      Name = "uptime-rds-security-group"
  }
}

resource "aws_db_subnet_group" "uptime-main" {
  name       = "uptime-db-subnet-group"
  subnet_ids = [
    aws_subnet.uptime-main.id,
    aws_subnet.uptime-sub.id
  ]

  tags = {
    Name = "uptime-db-subnet-group"
  }
}

resource "aws_db_instance" "uptime-mysql" {
  allocated_storage = 20
    engine          = "mysql"
    engine_version  = "8.0"
    instance_class  = "db.t4g.micro"
    db_name         = var.db_name
    username        = var.db_username
    password        = var.db_password
    parameter_group_name = "default.mysql8.0"
    db_subnet_group_name = aws_db_subnet_group.uptime-main.name
    vpc_security_group_ids = [aws_security_group.uptime-rds_sg.id]
    multi_az = false
    skip_final_snapshot = true
}

### Secrets Manager
resource "aws_secretsmanager_secret" "uptime-db-secret" {
  name        = "uptime-db-secret-${local.secrets_version}"
  description = "Secret for Uptime DB"
}

resource "aws_secretsmanager_secret_version" "uptime-db-secret-version" {
  secret_id     = aws_secretsmanager_secret.uptime-db-secret.id
  secret_string = jsonencode({
    username = var.db_username
    password = var.db_password
  })
  depends_on = [aws_db_instance.uptime-mysql]
}

resource "aws_secretsmanager_secret" "github-oauth-secret" {
  name        = "uptime-github-oauth-secret-${local.secrets_version}"
  description = "Secret for Uptime GitHub OAuth"
}

## IAM Role and Policy for EC2 to access Secrets Manager
resource "aws_secretsmanager_secret_version" "github-oauth-secret-version" {
  secret_id     = aws_secretsmanager_secret.github-oauth-secret.id
  secret_string = jsonencode({
    GITHUB_CLIENT_ID     = var.OAuth2_client_id
    GITHUB_SECRET_KEY = var.OAuth2_client_secret
    GITHUB_REDIRECT_URI  = var.OAuth2_redirect_uri
    JWT_SECRET_KEY       = var.JWT_secret_key
    DB_URL_TEST          = var.DB_URL_TEST
    DB_USER             = var.DB_USER
    DB_PASSWORD         = var.DB_PASSWORD
    DB_URL_DEV          = var.DB_URL_DEV
  })
}

resource "aws_iam_role" "uptime-ec2-secrets-manager-role" {
    name               = "uptime-ec2-secrets-manager-role"
    assume_role_policy = jsonencode(var.ec2-secrets-manager-role-policy)
}

resource "aws_iam_policy" "iam-policy-for-secrets-manager" {
  name        = "uptime-secrets-manager-policy"
  description = "Policy to allow EC2 to access Secrets Manager"
  policy      = jsonencode({
    Version = "2012-10-17",
    Statement = [
      {
        Effect = "Allow",
        Action = "secretsmanager:GetSecretValue",
        Resource = [
          aws_secretsmanager_secret.github-oauth-secret.arn,
          aws_secretsmanager_secret.uptime-db-secret.arn
        ]
      }
    ]
  })
}

resource "aws_iam_role_policy_attachment" "attach-secrets-manager-policy" {
    role       = aws_iam_role.uptime-ec2-secrets-manager-role.name
    policy_arn = aws_iam_policy.iam-policy-for-secrets-manager.arn
}

resource "aws_iam_instance_profile" "uptime-ec2-instance-profile" {
    name = "uptime-ec2-instance-profile"
    role = aws_iam_role.uptime-ec2-secrets-manager-role.name
}



// EC2 Key Pair
resource "aws_key_pair" "uptime-backend-key-pair" {
    key_name   = "uptime-backend-key-pair"
    public_key = file("~/.ssh/uptime-backend-key-pair.pub")
    tags = {
        Name = "uptime-backend-key-pair"
    }
}

// EC2 Instance
resource "aws_instance" "web" {
    ami                    = var.ami_id
    instance_type         = var.instance_type
    subnet_id             = aws_subnet.uptime-main.id
    vpc_security_group_ids = [aws_security_group.uptime-backend_sg.id]
    //iam_instance_profile   = aws_iam_instance_profile.uptime-ec2-instance-profile.name
    associate_public_ip_address = true
    key_name = "uptime-backend-key-pair"

    tags = {
        Name = "web-server"
    }
  depends_on = [aws_key_pair.uptime-backend-key-pair]
}

// Elastic IP
resource "aws_eip_association" "web_eip" {
    instance_id = aws_instance.web.id
    allocation_id = var.elastic_ip
}

// ECR
resource "aws_ecr_repository" "uptime-ecr" {
    name = "uptime-ecr"
    image_tag_mutability = "MUTABLE"
    tags = {
        Name = "uptime-ecr"
    }
}