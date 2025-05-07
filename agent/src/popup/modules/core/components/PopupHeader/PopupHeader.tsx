import { ReactElement, ReactNode } from 'react';

import ExtensionRoundedIcon from '@mui/icons-material/ExtensionRounded';
import { Toolbar } from '@mui/material';

import classes from './PopupHeader.module.css';

export default function PopupHeader(props: { children?: ReactNode }): ReactElement {
    return (
        <Toolbar className={classes.PopupHeader} sx={{ boxShadow: 1 }}>
            <ExtensionRoundedIcon className={classes.PopupLogo} />
            <h1>Chrome Extension React</h1>
            {props.children}
        </Toolbar>
    );
}
