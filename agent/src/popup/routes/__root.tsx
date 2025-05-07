import { useEffect } from 'react';

import { Outlet, createRootRoute, useNavigate } from '@tanstack/react-router';

function NotFound() {
    const navigate = useNavigate();

    useEffect(() => {
        navigate({
            to: '/home-page'
        });
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, []);

    return null;
}

function Layout() {
    return <Outlet />;
}

export const Route = createRootRoute({
    component: Layout,
    notFoundComponent: NotFound
});
