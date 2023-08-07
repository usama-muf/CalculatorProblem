import { Alert, Grid, TextField } from '@mui/material';
import React, { useState } from 'react'
import AddNewParameterDialog from '../small-components/AddNewParameterDialog';
import useCustomFunction from '../small-components/useCustomFunction';
import { Button } from '@material-ui/core';


// Assumptions 
// CompanyName Length > 1
// ParemeterName lenght > 5
// ParemeterValue should be Number



export default function NewCrsDashBoard({ dataSubmissionStatus }) {

    const { handlePostRequestCrs } = useCustomFunction();

    const [companyName, setCompanyName] = useState('');
    const [error, setError] = useState(null);
    const [map, setMap] = useState(new Map());
    const [isSubmitSuccessful, setIsSubmitSuccessful] = useState(false)


    const tableData = Array.from(map);

    const handleCompanyNameChange = (event) => {
        setCompanyName(event.target.value);
    };

    const handleParameterNameChange = () => {

    }
    const handleParameterValueChange = () => {

    }

    // Handles submit button click
    const handleSubmit = () => {
        let isValid = true;
        let nameError = '';


        if (companyName.length < 1) {
            isValid = false;
            nameError = 'Company Name should be at least 2 characters long';
        }

        //the list of company parameters and there scores must exist 
        if (map.size < 1) {
            isValid = false;
            nameError = "Must have some attributes before submitting";
        }

        if (!isValid) {
            // Update the state to display the validation errors
            setError(nameError);
            return;
        }

        const data = {
            'companyName': companyName,
            'parameters': convertMapToParameters(map)
        }
        handlePostRequestCrs(data);
        console.log("data send for creation ", data);
        setError(null);
        setIsSubmitSuccessful(true);
        dataSubmissionStatus(isSubmitSuccessful);

        //open a promt which states what to do next and show buttons as well
        // showStepper();
        // setTimeout(() => {
        //     setIsSubmitSuccessful(true);
        //     setIsDialogOpen(true); // Open the dialog box after successful submission
        // }, 500);
        // setIsSubmitSuccessful(true);

    };

    // Function to add values to the map and update the table data
    const addToMap = async (key, value) => {
        if (!isNaN(value) && key.length > 5 && value > 1 && value <= 100) {

            setMap(prevMap => {
                const updatedMap = new Map(prevMap);
                updatedMap.set(key.trim(), value.trim());
                setError(null);
                return updatedMap;
            });
        }
        else
            setError("Risk Parameter Value must be a number. ");
    }


    const deleteFromMap = async (key) => {
        setMap(prevMap => {
            const updatedMap = new Map(prevMap);
            updatedMap.delete(key);
            return updatedMap;
        });
    }

    // this function coverts our map to required ds which is to be send to databases.
    const convertMapToParameters = (map) => {
        const parameters = {};
        for (const [key, value] of map.entries()) {
            parameters[key] = value;
        }
        return parameters;
    };

    return (
        <div className='crs-add-dashboard'>

            <div><h3>Company Risk Score Dashboard : Add</h3></div>

            {error && <Alert severity="error">{error}</Alert>}

            <div>
                <Grid item xs={8}>
                    <TextField
                        autoFocus
                        margin="dense"
                        id="element_name"
                        label="Company Name"
                        type="text"
                        required
                        value={companyName}
                        onChange={handleCompanyNameChange}
                    />
                </Grid>
                {/* code to display added params on the page using textboxes */}
                {tableData.map(([key, value]) => (
                    <Grid container spacing={3} key={key}>
                        <Grid item xs={5}>
                            <TextField
                                autoFocus
                                margin="dense"
                                id="paremeter_name"
                                label="Paremeter Name"
                                fullWidth
                                type="text"
                                disabled
                                value={key}
                            />
                        </Grid>
                        <Grid item xs={5}>
                            <TextField
                                margin="dense"
                                id="paremeter_value"
                                label="Paremeter Value"
                                type="number"
                                fullWidth
                                disabled
                                error={error !== ''}
                                value={value}
                            />
                        </Grid>
                        <Grid item xs={2}>
                            <Button onClick={() => deleteFromMap(key)}>🗑️</Button>
                        </Grid>
                    </Grid>
                ))}

                {/* Plus button functionality */}
                <AddNewParameterDialog onParameterSubmit={addToMap} />

                {/* Submit Button  */}
                <Button variant="outlined" onClick={handleSubmit}>Submit</Button>
            </div>
            {/* <Dialog open={isDialogOpen} onClose={handleCloseDialog} fullWidth={true} maxWidth={'xl'}>
                <DialogTitle>Stepper Dialog</DialogTitle>
                <DialogContent>
                    {isSubmitSuccessful && <HorizontalLinearStepper currentStepLvl={1} />}
                </DialogContent>
            </Dialog> */}

        </div>
    )
}
