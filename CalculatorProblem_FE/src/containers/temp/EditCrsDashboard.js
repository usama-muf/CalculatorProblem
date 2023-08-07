import { Grid, Skeleton, TextField } from '@mui/material';
import React, { useEffect, useState } from 'react'
import { useParams } from 'react-router-dom'
// import AddIcon from '@mui/icons-material/Add'
import './EditCrsDashboard.css'
import AddNewParameterDialog from '../small-components/AddNewParameterDialog';
import useCustomFunction from '../small-components/useCustomFunction'
import { Button } from '@material-ui/core';


export default function EditCrsDashboard() {

    const { companyName } = useParams();
    const [rowData, setRowData] = useState(null);
    const [parameterMap, setParameterMap] = useState(new Map());
    const { handlePutRequestCrs } = useCustomFunction();



    useEffect(() => {
        const fetchData = async () => {
            try {
                const response = await fetch(`http://localhost:8092/v1/api/crs/get/${companyName}`);
                const responseData = await response.json();
                setRowData(responseData);
            } catch (error) {
                console.error('Error fetching row data:', error);
            }
        };

        fetchData();
    }, [companyName]);




    useEffect(() => {

        const setParameterMapFn = () => {
            console.log("Inside useEffect setParameterMapFn");
            const initialMap = new Map();
            rowData.parameters.forEach((parameter) => {
                initialMap.set(parameter.parameterName, parameter.parameterValue);
            });
            setParameterMap(initialMap);
        };

        if (rowData) {
            setParameterMapFn();
        }
    }, [rowData, setParameterMap]);

    const handleContent = async (e, parameterName) => {

        const { value } = e.target;
        setParameterMap(prevState => {
            const newState = new Map(prevState);
            newState.set(parameterName, value);
            return newState;
        });
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

    const handleNewParameterSubmit = (parameterName, parameterValue) => {
        // Use the parameterName and parameterValue in your main component's logic
        console.log('Parameter Name:', parameterName);
        console.log('Parameter Value:', parameterValue);

        setParameterMap(parameterMap.set(parameterName, parameterValue));
        // handlePutRequest();

        handlePutRequestCrs(companyName, parameterMap);

        console.log(parameterMap);
    };
    return (
        <>
            <div>
                <h3>Company Dimension Scores Dashboard : Edit</h3>
            </div>
            <div className='crs-dashboard-edit'>
                <div>
                    <TextField
                        id="outlined-read-only-input"
                        label="Company Name"
                        defaultValue={rowData.companyName}
                        fullWidth
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

                    <Grid container spacing={2}>
                        {rowData.parameters.map((parameter, index) => (
                            < Grid key={index} container item spacing={2} >
                                <Grid item xs={8}>
                                    <TextField disabled label="Parameter Name" fullWidth value={parameter.parameterName} />
                                </Grid>
                                <Grid item xs={4}>
                                    <TextField label="Parameter Value" fullWidth type='number' required
                                        onChange={(e) => handleContent(e, parameter.parameterName)}
                                        value={parameterMap.has(parameter.parameterName) ? parameterMap.get(parameter.parameterName) : ''}
                                    />
                                </Grid>
                            </Grid>
                        ))}
                    </Grid>
                </div >
                <div>
                    <AddNewParameterDialog onParameterSubmit={handleNewParameterSubmit} />
                </div>

                {/* <Button variant="outlined" onClick={handlePutRequest}>Outlined</Button> */}
                {/* <Button variant="outlined" onClick={sendData}>Outlined</Button> */}
                <div className='submit-btn'>
                    <Button variant="outlined" onClick={() => handlePutRequestCrs(companyName, parameterMap)}>Submit</Button>
                </div>
            </div>
        </>
    )
}
