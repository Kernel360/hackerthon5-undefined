import React, { Suspense } from 'react';

import { createBrowserRouter, Outlet, RouterProvider } from 'react-router-dom';

import { PrivateRoute } from '@/components/auth/PrivateRoute';
import { Toaster } from '@/components/ui/toaster';
import WaitingView from '@/components/WaitingView';
import { AuthProvider } from '@/contexts/auth';
import AuthLayout from '@/layouts/AuthLayout';
import DefaultLayout from '@/layouts/DefaultLayout';
import InterviewLayout from '@/layouts/InterviewLayout';
import NotFoundPage from '@/pages/error/NotFound';
import { AuthGuard } from '@/components/auth/AuthGuard';

// Lazy loading for pages
const LoginPage = React.lazy(() => import('@/pages/auth/Login'));
const SignupPage = React.lazy(() => import('@/pages/auth/Signup'));
const GithubCallback = React.lazy(() => import('@/pages/auth/GithubCallback'));
const Today = React.lazy(() => import('@/pages/today/Today'));
const Introductions = React.lazy(() => import('@/pages/Introductions'));
const DomainDetail = React.lazy(() => import('@/pages/domain/DomainDetail'));

const Root = () => {
  return (
    <AuthProvider>
      <Outlet />
      <Toaster />
    </AuthProvider>
  );
};

const router = createBrowserRouter([
  {
    element: <Root />,
    errorElement: <NotFoundPage />,
    children: [
      {
        path: '/',
        element: (
          <Suspense fallback={<WaitingView />}>
            <DefaultLayout />
          </Suspense>
        ),
        children: [
              { index: true, element: <Introductions/> },
              { path: 'metrics/today', element: <Today /> },
              { path: 'domain/:domainName', element: <DomainDetail /> },
        ],
      },
      {
        path: '/',
        element: (
          <Suspense fallback={<WaitingView />}>
            <AuthLayout />
          </Suspense>
        ),
        children: [
          { path: 'login', element: (
            <AuthGuard>
              <LoginPage />
            </AuthGuard>
          ) },
          { path: 'signup', element: <SignupPage /> }
        ]
      },
      {
        path: 'auth/github',
        element: <GithubCallback />
      },
    ]
  }
]);

export default function AppRoutes() {
  return <RouterProvider router={router} />;
}

