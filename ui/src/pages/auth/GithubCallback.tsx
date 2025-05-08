import { useEffect } from 'react';
import { useNavigate, useSearchParams } from 'react-router-dom';
import { Card } from '@/components/ui/card';
import { ReloadIcon } from '@radix-ui/react-icons';
import { axiosInstance } from '@/lib/axios';
import { Provider, OAuthProfileResponse, SignInRequest, SignUpRequest, AuthResponse } from '@/types/auth';
import { useAuth } from '@/contexts/auth';
import { useToast } from '@/hooks/use-toast';

export default function GithubCallback() {
  const navigate = useNavigate();
  const [searchParams] = useSearchParams();
  const { login } = useAuth();
  const { toast } = useToast();

  useEffect(() => {
    const handleGithubCallback = async () => {
      const code = searchParams.get('code');
      
      if (!code) {
        toast({
          variant: "destructive",
          title: "인증 오류",
          description: "GitHub 인증 코드를 찾을 수 없습니다.",
        });
        navigate('/login');
        return;
      }

      try {
        const profileResponse = await axiosInstance.get<OAuthProfileResponse>('/api/v1/auth/oauth/profile', {
          params: {
            provider: 'GITHUB' as Provider,
            code: code,
            redirectUrl: import.meta.env.VITE_APP_GITHUB_REDIRECT_URL
          }
        });

        const { providerId, avatarUrl, isRegistered } = profileResponse.data;

        let authResponse: AuthResponse;

        if (isRegistered) {
          const signInResponse = await axiosInstance.post<AuthResponse>('/api/v1/auth/sign-in', {
            provider: 'GITHUB',
            providerId
          } as SignInRequest);
          
          authResponse = signInResponse.data;
        } else {
          const nickname = `user_${Math.random().toString(36).substring(2, 8)}`;
          
          const signUpResponse = await axiosInstance.post<AuthResponse>('/api/v1/auth/sign-up', {
            provider: 'GITHUB',
            providerId,
            avatarUrl,
            nickname
          } as SignUpRequest);
          
          authResponse = signUpResponse.data;
        }

        login(authResponse.accessToken);
        toast({
          title: "로그인 성공",
          description: "환영합니다!",
        });
        navigate('/');

      } catch (error) {
        console.error('Authentication failed:', error);
        toast({
          variant: "destructive",
          title: "인증 실패",
          description: "GitHub 로그인 중 오류가 발생했습니다.",
        });
        navigate('/login');
      }
    };

    handleGithubCallback();
  }, [navigate, searchParams, login, toast]);

  return (
    <div className="container flex h-screen w-screen flex-col items-center justify-center">
      <Card className="w-[350px] p-8">
        <div className="flex flex-col items-center space-y-4">
          <ReloadIcon className="h-12 w-12 animate-spin text-primary" />
          <div className="space-y-2 text-center">
            <h2 className="text-2xl font-semibold tracking-tight">
              인증 처리 중...
            </h2>
            <p className="text-sm text-muted-foreground">
              GitHub 인증을 완료하고 있습니다. 잠시만 기다려주세요.
            </p>
          </div>
        </div>
      </Card>
    </div>
  );
}
