import React, { useState } from 'react';
import {
    Button,
    Dialog,
    DialogActions,
    DialogContent,
    DialogTitle,
    Fab,
    Grid,
    TextField,
    Tooltip
} from '@material-ui/core';
import AddIcon from '@mui/icons-material/Add'
import { Fade, IconButton } from '@mui/material';

const AddNewParameterDialog = ({ onParameterSubmit }) => {
    const [open, setOpen] = useState(false);
    const [parameterName, setParameterName] = useState('');
    const [parameterValue, setParameterValue] = useState('');
    const [nameError, setNameError] = useState('');
    const [valueError, setValueError] = useState('')

    const handleClickOpen = () => {
        setOpen(true);
    };

    const handleClose = () => {
        setOpen(false);
    };

    const handleParameterNameChange = (event) => {
        const newValue = event.target.value;
        setNameError('');
        // Validate the input using regular expression
        if (/^[a-z_]*$/.test(newValue)) {
            setParameterName(newValue);
        }

        else {
            setNameError('Only Lowecase Letters and Underscores allowed');
        }

        // setParameterName(event.target.value);
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

        if (isNaN(parameterValue) || parameterValue < 0 || parameterValue > 100 || parameterValue === null || parameterValue === '') {
            isValid = false;
            valueError = 'Parameter Value should be a number between 0 and 100';
            // setValueError(valueError)
            // return false;
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
        setOpen(false);
    };

    return (
        <div>
            <Tooltip TransitionComponent={Fade} TransitionProps={{ timeout: 600 }} title="Add new parameter" placement="left" >
                <Fab style={{ margin: '2vh', float: 'right' }} color="primary" aria-label="add" onClick={handleClickOpen}>
                    <AddIcon />
                </Fab>
            </Tooltip>
            <Dialog open={open} onClose={handleClose}>
                <DialogTitle>Add New </DialogTitle>
                <DialogContent>
                    <Grid container spacing={2}>
                        <Grid item xs={6}>
                            <TextField
                                autoFocus
                                focused
                                margin="dense"
                                id="newParameterName"
                                label="Parameter Name"
                                fullWidth
                                type="text"
                                required
                                value={parameterName}
                                onChange={handleParameterNameChange}
                                error={!!nameError}
                                helperText={nameError}

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
                                error={!!valueError}
                                helperText={valueError}
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

export default AddNewParameterDialog;
