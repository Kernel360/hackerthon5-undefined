import { Suspense } from 'react';

import { SnackbarProvider } from 'notistack';

import { ThemeProvider, createTheme } from '@mui/material/styles';
import { RouterProvider, createRouter } from '@tanstack/react-router';

import PopupHeader from '@/popup/modules/core/components/PopupHeader/PopupHeader';

import AlertSnackbar from './modules/core/components/AlertSnackbar';
import { routeTree } from './routeTree.gen';

const router = createRouter({
    routeTree: routeTree
});

// Register the router instance for type safety
declare module '@tanstack/react-router' {
    interface Register {
        router: typeof router;
    }
}

const theme = createTheme();

export default function App() {
    return (
        <ThemeProvider theme={theme}>
            <SnackbarProvider
                Components={{
                    error: AlertSnackbar,
                    success: AlertSnackbar,
                    warning: AlertSnackbar,
                    info: AlertSnackbar
                }}
            >
                <Suspense fallback={<PopupHeader />}>
                    <RouterProvider router={router} />
                </Suspense>
            </SnackbarProvider>
        </ThemeProvider>
    );
}
