import React, { useState } from 'react'
import { Alert, Button, Grid, TextField } from '@mui/material';
import useCustomFunction from '../small-components/useCustomFunction';


export default function NewRslDashboard() {

    const { handlePostRiskScoreLevel } = useCustomFunction();

    const [elementName, setElementName] = useState('');
    const [formula, setFormula] = useState('')
    const [nameError, setNameError] = useState('');
    const [valueError, setValueError] = useState('');
    const [checkboxChecked, setCheckboxChecked] = useState(false);

    const [minimum, setMinimum] = useState('');
    const [maximum, setMaximum] = useState('');
    const [message, setMessage] = useState('');
    const [errorMessage, setErrorMessage] = useState('');
    const [alertType, setAlertType] = useState('')




    const handleMinimumChange = (event) => {
        setMinimum(event.target.value);
        setErrorMessage('');

    };
    const handleMaximumChange = (event) => {
        setErrorMessage('');
        setMaximum(event.target.value);
    };

    const handleMessageChange = (event) => {
        setErrorMessage('');
        setMessage(event.target.value);
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        // Validation and formatting of input values
        const min = parseInt(minimum);
        const max = parseInt(maximum);
        if (isNaN(min) || isNaN(max) || min >= max || min < 0 || max < 0 || min > 100 || max > 100) {
            setErrorMessage('Please enter valid integer values with minimum < maximum.');
            setAlertType('warning');
            return;
        }

        // Clear any previous error message
        setErrorMessage('');

        const request = {
            "minScore": minimum,
            "maxScore": maximum,
            "level": message.trim()
        }

        const response = await handlePostRiskScoreLevel(request);

        console.log(request, response);
        setAlertType(response.severity);
        setErrorMessage(response.message);


        if (response.severity === 'success')
            removeValuesFromTextBoxes();



    };

    const removeValuesFromTextBoxes = async () => {
        setMinimum('');
        setMessage('');
        setMaximum('');
    }


    return (
        <div className='rcl-dashboard'>

            <div>
                <h3>Add New Score Level : Score Dashboard</h3>
            </div>

            <Grid container spacing={2}>
                <Grid item xs={3}>
                    <TextField
                        autoFocus
                        margin="dense"
                        // id="element_name"
                        label="Lower Limit"
                        fullWidth
                        type="number"
                        required
                        // error={nameError !== ''}
                        value={minimum}
                        onChange={handleMinimumChange}
                    />
                </Grid>
                <Grid item xs={3}>
                    <TextField
                        margin="dense"
                        // id="element_name"
                        label="Upper Limit"
                        fullWidth
                        type="number"
                        required
                        // error={'hi'}
                        value={maximum}
                        onChange={handleMaximumChange}
                    />
                </Grid>
                <Grid item xs={6}>
                    <TextField
                        margin="dense"
                        // id="formula"
                        label="Message"
                        type="text"
                        fullWidth
                        required
                        // error={valueError !== ''}
                        value={message}
                        onChange={handleMessageChange}
                    />
                </Grid>
            </Grid>

            <Button onClick={handleSubmit} variant="contained">Submit</Button>

            {errorMessage && <Alert severity={alertType}>{errorMessage}</Alert>}


        </div>
    )
}