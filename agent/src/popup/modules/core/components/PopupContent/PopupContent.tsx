import { ReactElement, ReactNode } from 'react';

import Box from '@mui/material/Box';

import classes from './PopupContent.module.css';

export default function PopupContent(props: { children?: ReactNode }): ReactElement {
    return (
        <Box
            className={classes.PopupContent}
            display="flex"
            justifyContent="center"
            alignItems="center"
        >
            {props.children}
        </Box>
    );
}
