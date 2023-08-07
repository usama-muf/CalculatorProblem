import React, { useState, useEffect } from 'react';
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableRow from '@mui/material/TableRow';
import Paper from '@mui/material/Paper';
import './OutputTable.css'
import { Link } from 'react-router-dom';
import { Button, Card, CardActions, CardContent, Grid, Skeleton, Typography } from '@mui/material';

const OutputTable = () => {
    const [outputs, setOutput] = useState(null);
    const [headers, setHeader] = useState([]);

    useEffect(() => {
        const fetchData = async () => {
            try {
                const header = await fetch('http://localhost:8092/v1/api/output/outputTblParams');
                const response = await fetch('http://localhost:8092/v1/api/output/calculate');

                const headerData = await header.json();
                const responseData = await response.json();
                console.log('HeaderData', headerData, "responseData", responseData);


                // Check if responseData is an array
                if (Array.isArray(responseData)) {
                    setOutput(responseData);
                } else {
                    console.error('Invalid response format:', responseData);
                    setOutput([]);
                }
                if (Array.isArray(headerData)) {
                    setHeader(headerData);
                } else {
                    console.error('Invalid response format:', headerData);
                    setHeader([]);
                }
            } catch (error) {
                console.error('Error fetching data:', error);
                setOutput([]);
                setHeader([]);
            }
        };

        fetchData();
    }, []);

    const HandleReloadClick = async () => {

        const header = await fetch('http://localhost:8092/v1/api/output/outputTblParams');
        const response = await fetch('http://localhost:8092/v1/api/output/trigger');

        const headerData = await header.json();
        const responseData = await response.json();
        console.log('HeaderData', headerData, "responseData", responseData);


        // Check if responseData is an array
        if (Array.isArray(responseData)) {
            setOutput(responseData);
        } else {
            console.error('Invalid response format:', responseData);
            setOutput([]);
        }
        if (Array.isArray(headerData)) {
            setHeader(headerData);
        } else {
            console.error('Invalid response format:', headerData);
            setHeader([]);
        }
    }
    // Generate the table header using the company names and parameter names
    const header =
        headers.length > 0
            ? ['CompanyName', ...headers]
            : [];


    if (!outputs || outputs.length < 1) {
        // Render loading state or fallback UI while fetching the row data

        return (
            <div>

                <div className='skeleton-div'>
                    <Skeleton animation="wave" variant="text" />
                    <Skeleton animation="wave" variant="text" />
                    <Skeleton animation="wave" variant="text" />
                </div>

                < div className='btn-container' >
                    <Button className='button' variant="outlined" color="primary" onClick={HandleReloadClick}>
                        Reload Output Table
                    </Button>
                    <Link to='/edit'>
                        <Button className='button' variant="outlined" color="primary">
                            Manage Existing Tables
                        </Button>
                    </Link>
                </div>
            </div>
        );
    }

    return (
        <div className='table-container'>
            < div className='btn-container' >

                <Button className='button' variant="outlined" color="primary" onClick={HandleReloadClick}>
                    Reload Output Table
                </Button>
                <Link to='/edit'>
                    <Button className='button' variant="outlined" color="primary">
                        Manage Existing Tables
                    </Button>
                </Link>
            </div>

            <Grid container spacing={2}>

                {outputs && outputs.map((row, index) => (
                    <Grid item xs={6} key={row.companyName + index}>
                        < Card sx={{ minWidth: 50, maxWidth: 500, margin: 1 }}>
                            <CardContent>
                                < Typography sx={{ fontSize: 15, textAlign: 'center', fontWeight: 1000 }} color="text.secondary" gutterBottom>
                                    {row.companyName}
                                </Typography>
                                <TableContainer component={Paper}>
                                    <Table sx={{ minWidth: 50 }} aria-label="a dense table">
                                        <TableBody>

                                            {row.outputParameters && row.outputParameters.map((params, index) => (
                                                <TableRow
                                                    key={params.parameterName + index}
                                                    sx={{ '&:last-child td, &:last-child th': { border: 0 } }}
                                                >
                                                    <TableCell component="th" scope="row">
                                                        {(index + 1)}
                                                    </TableCell>
                                                    <TableCell align="right">{params.parameterName}</TableCell>
                                                    <TableCell align="right">{params.parameterValue}</TableCell>
                                                </TableRow>
                                            ))}
                                        </TableBody>
                                    </Table>
                                </TableContainer>
                            </CardContent>
                            <CardActions>
                                <Button size="small">EDIT</Button>
                            </CardActions>
                        </Card>
                    </Grid>
                ))
                }
            </Grid>
        </div >

    );
};

export default OutputTable;
