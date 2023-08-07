import React, { useState } from 'react'
import './RclDashboard.css'
import { Alert, Box, Button, Checkbox, FormControlLabel, Grid, TextField } from '@mui/material';
import useCustomFunction from '../small-components/useCustomFunction';
import { CheckBox } from '@mui/icons-material';

export default function NewRclDashboard() {


    const { handlePostRequestRcl } = useCustomFunction();

    const [elementName, setElementName] = useState('');
    const [formula, setFormula] = useState('')
    const [nameError, setNameError] = useState('');
    const [valueError, setValueError] = useState('');
    const [checkboxChecked, setCheckboxChecked] = useState(false);
    const [responseMessage, setresponseMessage] = useState('')
    const [alertSeverity, setAlertSeverity] = useState('')


    const handleElementNameChange = (event) => {
        const newValue = event.target.value;
        setNameError('');
        // Validate the input using regular expression
        if (/^[a-z_]*$/.test(newValue)) {
            setElementName(newValue);
        }

        else {
            setNameError('Only Lowecase Letters and Underscores allowed');
        }
    };

    const handleFormulaChange = (event) => {
        setFormula(event.target.value);
    };

    // const handleCheckboxChange = (event) => {
    //     setCheckboxChecked(event.target.checked);
    // };

    const handleSubmit = async () => {
        let isValid = true;
        let nameError = '';
        let valueError = '';


        if (elementName.length < 5) {
            isValid = false;
            nameError = 'Parameter Name should be at least 5 characters long';
        }
        if (formula.length < 5) {
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
        const data = {
            elementName: elementName.trim(),
            formula: formula.trim(),
            // isDependentOnOtherFormula: checkboxChecked,
        }

        // Reset the form
        const response = await handlePostRequestRcl(data);

        if (!response.valid) {
            setresponseMessage(response.message);
            setAlertSeverity('error')
            return;
        }

        else {
            setresponseMessage(response.message);
            setAlertSeverity('success');

            setTimeout(() => {

                setresponseMessage('');
                setElementName('');
                setFormula('');
            }, 500);
            return;
        }


        // Close the dialog
    };




    return (
        <div className='rcl-dashboard'>

            <div>
                <h3>Risk Calculation Logic Dashboard : New</h3>
            </div>
            <Grid container spacing={2}>
                <Grid item xs={6}>
                    <TextField
                        autoFocus
                        margin="dense"
                        id="element_name"
                        label="Element Name"
                        fullWidth
                        type="text"
                        required
                        value={elementName}
                        onChange={handleElementNameChange}
                        error={!!nameError}
                        helperText={nameError}
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
                        value={formula}
                        onChange={handleFormulaChange}
                    />
                </Grid>
            </Grid>

            {/* <FormControlLabel
                control={<Checkbox checked={checkboxChecked} onChange={handleCheckboxChange} />}
                label="Is this formula somehow dependent on other formula's ?"
            /> */}
            {responseMessage && (
                <Box display="flex" justifyContent="center" mt={2}>
                    <Alert severity={alertSeverity}>{responseMessage}</Alert>
                </Box>
            )}
            <Button onClick={handleSubmit}>Submit</Button>
        </div>
    )
}
