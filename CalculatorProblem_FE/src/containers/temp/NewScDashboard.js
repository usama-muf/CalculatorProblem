import React, { useEffect, useState } from 'react'
import './RclDashboard.css'
import { Alert, Box, Button, Checkbox, FormControl, FormControlLabel, Grid, InputLabel, MenuItem, Select, TextField } from '@mui/material';
import useCustomFunction from '../small-components/useCustomFunction';
import { CheckBox } from '@mui/icons-material';
import { useNavigate } from 'react-router';

export default function NewScDashboard() {


    const { handlePostRequestSc, handleGetUniqueLevelName } = useCustomFunction();

    const [error, setError] = useState('');
    const [conditions, setConditions] = useState('')
    const [selectedCondtion, setSelectedCondtion] = useState('')
    const [conditionCount, setConditionCount] = useState('')
    const [value, setValue] = useState('')
    const [responseMessage, setResponseMessage] = useState('')
    const [severity, setSeverity] = useState('')

    const navigate = useNavigate();

    useEffect(() => {
        const fetchAllConditions = async () => {
            const response = await handleGetUniqueLevelName();
            setConditions(response);
        }

        fetchAllConditions();

    }, [])


    const handleConditionChange = (event) => {
        setSelectedCondtion(event.target.value);
    };

    const handleCountChange = (event) => {
        setConditionCount(event.target.value);
    };
    const handleValueChange = (event) => {
        setValue(event.target.value);
    };

    console.log(conditions)
    const handleSubmit = async () => {

        const parsedCount = parseInt(conditionCount);
        const parsedValue = parseInt(value);

        if (Number.isNaN(parsedCount) || Number.isNaN(parsedValue)) {
            setError('Please enter valid numeric values.');
            return;
        }

        if (parsedCount < 0 || parsedCount > 10 || parsedValue < 0 || parsedValue > 100) {
            setError('Scores should be in limit.');
            return;
        }


        const data = {
            selectedCondtion, conditionCount, value
        }
        const response = await handlePostRequestSc(data);

        setSeverity(response.severity);
        setResponseMessage(response.message)

        if (response.severity === 'success') {
            setTimeout(() => {
                navigate('/scdash', { replace: true })
            }, 1000);
        }

        console.log(response);


    };




    return (
        <div className='rcl-dashboard'>

            <div>
                <h3>Score Cap Dashboard: Add New</h3>
            </div>
            <Grid container spacing={2}>
                <Grid item xs={6}>
                    <Box sx={{ minWidth: 120 }}>
                        <FormControl fullWidth>
                            <InputLabel id="select-condition">Select Condition</InputLabel>
                            <Select
                                labelId="select-condition-label"
                                id="demo-simple-select"
                                value={selectedCondtion}
                                label="Select Condition "
                                onChange={handleConditionChange}
                            >
                                {conditions && conditions.map((condition, index) => (
                                    <MenuItem key={index} value={condition}>{condition}</MenuItem>

                                ))}
                            </Select>
                        </FormControl>
                    </Box>
                </Grid>
                <Grid item xs={3}>
                    <TextField
                        margin="dense"
                        id="count"
                        label="Condition Count"
                        type="number"
                        fullWidth
                        required
                        value={conditionCount}
                        onChange={handleCountChange}
                        error={!!error}
                        helperText={error} />
                </Grid>
                <Grid item xs={3}>
                    <TextField
                        margin="dense"
                        id="value"
                        label="Value"
                        type="number"
                        fullWidth
                        required
                        value={value}
                        onChange={handleValueChange}
                        error={!!error}
                        helperText={error}
                    />
                </Grid>
            </Grid>

            {responseMessage && (
                <Box display="flex" justifyContent="center" mt={2}>
                    <Alert severity={severity}>{responseMessage}</Alert>
                </Box>
            )}
            <Button onClick={handleSubmit}>Submit</Button>
        </div>
    )
}
