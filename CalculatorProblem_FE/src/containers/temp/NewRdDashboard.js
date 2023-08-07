import { Button, Grid, TextField } from '@mui/material';
import React, { useState } from 'react'

export default function NewRdDashboard() {

    const [dimensionName, setDimensionName] = useState('');
    const [dimensionWeight, setDimensionWeight] = useState('')
    const [nameError, setNameError] = useState('');
    const [valueError, setValueError] = useState('')



    const handleElementNameChange = (event) => {
        setDimensionName(event.target.value);
    };

    const handleFormulaChange = (event) => {
        setDimensionWeight(event.target.value);
    };

    const handleSubmit = () => {
        let isValid = true;
        let nameError = '';
        let valueError = '';


        if (dimensionName.length < 5) {
            isValid = false;
            nameError = 'Parameter Name should be at least 5 characters long';
        }
        if (dimensionWeight.length < 5) {
            isValid = false;
            nameError = 'Formula should be at least 5 characters long';
        }
        if (!isValid) {
            // Update the state to display the validation errors
            setNameError(nameError);
            setValueError(valueError);
            return;
        }
        // Call the onParameterSubmit function with the values
        // onParameterSubmit(parameterName, parameterValue);

        // Reset the form


        // Close the dialog
    };




    return (
        <div className='rcl-dashboard'>

            <div>
                <h3>Risk Dimension Dashboard : Add</h3>
            </div>
            <Grid container spacing={2}>
                <Grid item xs={6}>
                    <TextField
                        autoFocus
                        margin="dense"
                        id="element_name"
                        label="Dimension Name"
                        fullWidth
                        type="text"
                        required
                        error={nameError !== ''}
                        value={dimensionName}
                        onChange={handleElementNameChange}
                    />
                </Grid>
                <Grid item xs={6}>
                    <TextField
                        margin="dense"
                        id="formula"
                        label="Formula"
                        type="text"
                        fullWidth
                        required
                        error={valueError !== ''}
                        value={dimensionWeight}
                        onChange={handleFormulaChange}
                    />
                </Grid>
            </Grid>

            <Button onClick={handleSubmit}>Submit</Button>
        </div>
    )
}
