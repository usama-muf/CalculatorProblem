import { Alert, Box, Grid, Skeleton, TextField } from '@mui/material';
import React, { useEffect, useState } from 'react'
import { useNavigate, useParams } from 'react-router-dom'
// import AddIcon from '@mui/icons-material/Add'
import './EditCrsDashboard.css'
import AddNewParameterDialog from '../small-components/AddNewParameterDialog';
import useCustomFunction from '../small-components/useCustomFunction'
import { Button } from '@material-ui/core';


export default function EditScDashboard() {

    const { id } = useParams();
    const [rowData, setRowData] = useState(null);
    const [value, setValue] = useState(null)
    const [responseMessage, setResponseMessage] = useState('')
    const [severity, setSeverity] = useState('')
    const [error, setError] = useState('')

    const { handleGetScoreCapById, handlePutRequestSc } = useCustomFunction();
    const navigate = useNavigate();


    useEffect(() => {
        const fetchData = async () => {
            try {
                const response = await handleGetScoreCapById(id);
                // const responseData = await response.json();
                setRowData(response);
                setValue(response.totalRiskCappedScore)
            } catch (error) {
                console.error('Error fetching row data:', error);
            }
        };

        fetchData();
    }, []);




    // useEffect(() => {

    //     const setParameterMapFn = () => {
    //         console.log("Inside useEffect setParameterMapFn");
    //         const initialMap = new Map();
    //         rowData.parameters.forEach((parameter) => {
    //             initialMap.set(parameter.parameterName, parameter.parameterValue);
    //         });
    //         setParameterMap(initialMap);
    //     };

    //     if (rowData) {
    //         setParameterMapFn();
    //     }
    // }, [rowData, setParameterMap]);

    const handleValueChange = async (e) => {
        setValue(e.target.value);
    };


    if (!rowData) {
        // Render loading state or fallback UI while fetching the row data
        return (

            <div className='crs-dashboard-edit' >
                <Skeleton animation="wave" variant="text" />
                <Skeleton animation="wave" variant="text" />
                <Skeleton animation="wave" variant="text" />
            </div>
        );
    }

    const handleSubmit = async () => {

        const data = {
            value: value,
        }

        const parsedValue = parseInt(value);

        if (Number.isNaN(parsedValue)) {
            setError('Please enter valid numeric values.');
            return;
        }

        if (parsedValue < 0 || parsedValue > 100) {
            setError('Scores should be in limit.');
            return;
        }



        const response = await handlePutRequestSc(id, data);

        setSeverity(response.severity);
        setResponseMessage(response.message)

        if (response.severity === 'success') {
            setTimeout(() => {
                navigate('/scdash', { replace: true })
            }, 1000);
        }

        console.log(response);
        console.log(response);

    };


    return (
        <>
            <div>
                <h3>Company Dimension Scores Dashboard : Edit</h3>
            </div>
            <div className='crs-dashboard-edit'>
                <div>
                    <Grid container spacing={2}>
                        <Grid item xs={4}>
                            <TextField
                                id="outlined-read-only-input"
                                label="Condition"
                                defaultValue={rowData.scoreCapCondition}
                                InputProps={{
                                    readOnly: true,
                                    style: {
                                        fontSize: '20px',
                                        fontWeight: 'bold',
                                        fontFamily: 'sans-serif',
                                        marginBottom: '2vh'
                                    }
                                }}
                                margin='dense'
                            />
                        </Grid>
                        <Grid item xs={4}>
                            <TextField
                                id="outlined-read-only-input"
                                label="Condition Count"
                                defaultValue={rowData.countCondition}
                                InputProps={{
                                    readOnly: true,
                                    style: {
                                        fontSize: '20px',
                                        fontWeight: 'bold',
                                        fontFamily: 'sans-serif',
                                        marginBottom: '2vh'
                                    }
                                }}
                                margin='dense'
                            />
                        </Grid>
                        <Grid item xs={4}>
                            <TextField
                                label="Value"
                                type='number'
                                focused
                                required
                                onChange={handleValueChange}
                                value={value}
                                InputProps={{
                                    style: {
                                        fontSize: '20px',
                                        fontWeight: 'lighter',
                                        fontFamily: 'cursive',
                                        marginBottom: '2vh'
                                    }
                                }}
                                margin='dense'
                                error={!!error}
                                helperText={error}
                            />
                        </Grid>
                    </Grid>
                </div >

                {responseMessage && (
                    <Box display="flex" justifyContent="center" mt={2}>
                        <Alert severity={severity}>{responseMessage}</Alert>
                    </Box>
                )}

                <div className='submit-btn'>
                    <Button variant="outlined" onClick={handleSubmit}>Submit</Button>
                </div>
            </div>
        </>
    )
}
