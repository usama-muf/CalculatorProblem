import * as React from 'react';
import InfoIcon from '@mui/icons-material/Info';
import IconButton from '@mui/material/IconButton';
import Tooltip from '@mui/material/Tooltip';


export const TooltipInfoIcon = ({ title }) => {
    return (
        <Tooltip title={title}>
            < IconButton >
                <InfoIcon />
            </IconButton >
        </Tooltip >
    )
}
