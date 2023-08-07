import React, { useState } from 'react';
import {
    Button,
    Dialog,
    DialogActions,
    DialogContent,
    DialogTitle,
    Grid,
    TextField,
} from '@material-ui/core';

const AddNewDialogBox = ({ heading, open, onClose, onParameterSubmit }) => {
    const [parameterName, setParameterName] = useState('');
    const [parameterValue, setParameterValue] = useState('');
    const [nameError, setNameError] = useState('');
    const [valueError, setValueError] = useState('')

    const handleClose = () => {
        onClose();
    };

    const handleParameterNameChange = (event) => {
        setParameterName(event.target.value);
    };

    const handleParameterValueChange = (event) => {
        setParameterValue(event.target.value);
    };

    const handleSubmit = () => {
        let isValid = true;
        let nameError = '';
        let valueError = '';

        if (parameterName.length < 5) {
            isValid = false;
            nameError = 'Parameter Name should be at least 5 characters long';
        }

        if (!isValid) {
            // Update the state to display the validation errors
            setNameError(nameError);
            setValueError(valueError);
            return;
        }
        // Call the onParameterSubmit function with the values
        onParameterSubmit(parameterName, parameterValue);

        // Reset the form
        setParameterName('');
        setParameterValue('');

        // Close the dialog
        onClose();

    };

    return (
        <div>
            <Dialog open={open} onClose={onclose}>
                <DialogTitle>{heading} </DialogTitle>
                <DialogContent>
                    <Grid container spacing={2}>
                        <Grid item xs={6}>
                            <TextField
                                autoFocus
                                margin="dense"
                                id="newParameterName"
                                label="Parameter Name"
                                fullWidth
                                type="text"
                                required
                                error={nameError !== ''}
                                value={parameterName}
                                onChange={handleParameterNameChange}
                            />
                        </Grid>
                        <Grid item xs={6}>
                            <TextField
                                margin="dense"
                                id="newParameterValue"
                                label="Parameter Value"
                                type="text"
                                fullWidth
                                required
                                error={valueError !== ''}
                                value={parameterValue}
                                onChange={handleParameterValueChange}
                            />
                        </Grid>
                    </Grid>
                </DialogContent>
                <DialogActions>
                    <Button onClick={handleClose}>Cancel</Button>
                    <Button onClick={handleSubmit}>Add</Button>
                </DialogActions>
            </Dialog>
        </div>
    );
};

export default AddNewDialogBox;
