import { Box, Typography } from '@mui/material';
import React from 'react'

export default function NoPage() {
    return (
        <Box
            sx={{
                display: 'flex',
                flexDirection: 'column',
                alignItems: 'center',
                justifyContent: 'center',
                height: '100vh',
            }}
        >
            <Typography variant="h1" gutterBottom>
                Page Not Found
            </Typography>
            <Typography variant="h5" color="text.secondary">
                Oops! The page you are looking for does not exist.
            </Typography>
            <Typography variant="h2">
                😔
            </Typography>
        </Box>
    );
};

