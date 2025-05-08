import { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '@/contexts/auth';

interface AuthGuardProps {
  children: React.ReactNode;
  requireAuth?: boolean;
  redirectTo?: string;
}

export const AuthGuard = ({ children, requireAuth = false, redirectTo = '/' }: AuthGuardProps) => {
  const { isAuthenticated, isLoading } = useAuth();
  const navigate = useNavigate();

  useEffect(() => {
    // 로딩이 완료된 후에만 리다이렉트 처리
    if (!isLoading) {
      if (requireAuth && !isAuthenticated) {
        // 인증이 필요한 페이지에서 비로그인 상태
        navigate('/login', { replace: true });
      } else if (!requireAuth && isAuthenticated) {
        // 로그인 페이지에서 이미 로그인 상태
        navigate(redirectTo, { replace: true });
      }
    }
  }, [isAuthenticated, isLoading, navigate, requireAuth, redirectTo]);

  // 로딩 중에는 아무것도 렌더링하지 않음
  if (isLoading) {
    return null;
  }

  // 인증이 필요한 페이지에서 비로그인 상태이거나
  // 로그인 페이지에서 이미 로그인 상태인 경우 렌더링하지 않음
  if ((requireAuth && !isAuthenticated) || (!requireAuth && isAuthenticated)) {
    return null;
  }

  return <>{children}</>;
}; 