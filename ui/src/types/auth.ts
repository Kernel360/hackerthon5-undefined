export type Provider = 'GITHUB';

export interface OAuthProfileResponse {
  providerId: string;
  avatarUrl: string;
  isRegistered: boolean;
}

export interface SignInRequest {
  provider: Provider;
  providerId: string;
}

export interface SignUpRequest {
  provider: Provider;
  providerId: string;
  avatar: string;
  nickname: string;
}

export interface AuthResponse {
  accessToken: string;
}
