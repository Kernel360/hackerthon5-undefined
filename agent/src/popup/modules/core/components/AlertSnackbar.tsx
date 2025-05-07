import { Ref } from 'react';

import { CustomContentProps, SnackbarContent, useSnackbar } from 'notistack';

import { Alert, AlertColor } from '@mui/material';

function AlertSnackbar({
    className,
    id,
    message,
    variant,
    ref
}: CustomContentProps & {
    ref: Ref<HTMLDivElement>;
}) {
    const { closeSnackbar } = useSnackbar();

    let severity: AlertColor;
    switch (variant) {
        case 'success':
            severity = 'success';
            break;
        case 'error':
            severity = 'error';
            break;
        case 'warning':
            severity = 'warning';
            break;
        case 'info':
            severity = 'info';
            break;
        case 'default':
        default:
            severity = 'info';
            break;
    }

    return (
        <SnackbarContent ref={ref}>
            <Alert
                className={className}
                severity={severity}
                style={{
                    width: '100%'
                }}
                onClose={() => closeSnackbar(id)}
            >
                {message}
            </Alert>
        </SnackbarContent>
    );
}

export default AlertSnackbar;
