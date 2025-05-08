import { useState } from "react"
import { Link } from "react-router-dom"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { Card, CardContent, CardDescription, CardFooter, CardHeader, CardTitle } from "@/components/ui/card"

export default function SignupPage() {
  const [formData, setFormData] = useState({
    email: "",
    password: "",
    confirmPassword: "",
    name: ""
  })

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault()
    // 회원가입 로직 구현
    console.log(formData)
  }

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target
    setFormData(prev => ({
      ...prev,
      [name]: value
    }))
  }

  return (
    <Card className="w-[350px]">
      <form onSubmit={handleSubmit}>
        <CardHeader>
          <CardTitle>회원가입</CardTitle>
          <CardDescription>새로운 계정을 만듭니다.</CardDescription>
        </CardHeader>
        <CardContent className="space-y-4">
          <div className="space-y-2">
            <Label htmlFor="email">이메일</Label>
            <Input 
              id="email"
              name="email" 
              type="email" 
              placeholder="name@example.com"
              value={formData.email}
              onChange={handleChange}
              required 
            />
          </div>
          <div className="space-y-2">
            <Label htmlFor="name">이름</Label>
            <Input 
              id="name"
              name="name" 
              value={formData.name}
              onChange={handleChange}
              required 
            />
          </div>
          <div className="space-y-2">
            <Label htmlFor="password">비밀번호</Label>
            <Input 
              id="password"
              name="password" 
              type="password"
              value={formData.password}
              onChange={handleChange}
              required 
            />
          </div>
          <div className="space-y-2">
            <Label htmlFor="confirmPassword">비밀번호 확인</Label>
            <Input 
              id="confirmPassword"
              name="confirmPassword" 
              type="password"
              value={formData.confirmPassword}
              onChange={handleChange}
              required 
            />
          </div>
        </CardContent>
        <CardFooter className="flex flex-col gap-4">
          <Button type="submit" className="w-full">회원가입</Button>
          <p className="text-sm text-center text-muted-foreground">
            이미 계정이 있으신가요?{" "}
            <Link to="/auth" className="text-primary hover:underline">
              로그인
            </Link>
          </p>
        </CardFooter>
      </form>
    </Card>
  )
} 