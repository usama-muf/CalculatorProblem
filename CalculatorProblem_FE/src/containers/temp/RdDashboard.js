import { Alert, Button, Dialog, DialogActions, DialogContent, DialogContentText, DialogTitle, Fade, Grid, IconButton, Paper, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, TextField, Tooltip } from '@mui/material';
import React, { useEffect, useRef, useState } from 'react'
import DeleteIcon from '@mui/icons-material/Delete';
import EditIcon from '@mui/icons-material/BorderColorTwoTone';
import './RdDashboard.css'

import useCustomFunction from '../small-components/useCustomFunction';



export default function RdDashboard() {
    const [tableData, setTableData] = useState([]);
    const iconButtonRef = useRef(null);
    const [open, setOpen] = React.useState(false);
    const [editOpen, setEditOpen] = React.useState(false);
    const [elementToDelete, setElementToDelete] = useState('');
    const [tempWeight, setTempWeight] = useState('')
    const [tempDimension, setTempDimensoin] = useState(null)
    const [totalWeight, setTotalWeight] = useState(0)
    const [error, setError] = useState('')
    const [totalWeightError, settotalWeightError] = useState('')

    const { handleGetAllRiskDimension, handleDeleteRequestRd, handlePutRequestRd } = useCustomFunction();



    // const fetchData = async () => {
    //     try {
    //         const response = await fetch('http://localhost:8092/v1/api/rd/dimension-wt');
    //         const responseData = await response.json();
    //         console.log(responseData);
    //         const sumOfWeights = responseData.reduce((sum, data) => sum + data.weight, 0);
    //         if (sumOfWeights !== 100) settotalWeightError("The sum of Weight must be equal to 100 !!");
    //         setTotalWeight(sumOfWeights);
    //         setTableData(responseData);

    //         // Check if responseData is an array
    //         if (Array.isArray(responseData)) {
    //             setTableData(responseData);
    //             console.log(tableData)
    //         } else {
    //             console.error('Invalid response format:', responseData);
    //             setTableData([]);
    //         }
    //     } catch (error) {
    //         console.error('Error fetching data:', error);
    //         setTableData([]);
    //     }
    // };

    useEffect(() => {
        const fetchRiskDimensions = async () => {
            const data = await handleGetAllRiskDimension();
            setTableData(data);
        };

        fetchRiskDimensions();
    }, []);

    useEffect(() => {
        checkWeightSum();
    }, [tableData]);

    const deleteRowByDimensionName = async (dimensionName) => {
        handleDeleteRequestRd(dimensionName);

    };


    const editRowByDimensionName = async (dimensionName, newWeight) => {
        return await handlePutRequestRd(dimensionName, newWeight);
    }

    const handleDeleteClickOpen = (dimension) => {
        setOpen(true);
        setElementToDelete(dimension);
    };

    const handleDeleteClose = () => {
        setOpen(false);
        setElementToDelete('');
    };

    const handleDeleteClick = async () => {
        console.log(`Deleting ${elementToDelete}`);
        deleteRowByDimensionName(elementToDelete);
        handleDeleteClose();
    };


    const handleEditClickOpen = (dimension, weight) => {
        setEditOpen(true)
        setTempDimensoin(dimension);
        setTempWeight(weight)
    };

    const handleEditClose = () => {
        setEditOpen(false)
        setTempWeight('');
        setTempDimensoin(null);
    };

    const handleWeightChange = (event) => {
        setTempWeight(event.target.value);
    };

    const handleEditClick = async () => {
        console.log(tempDimension);
        console.log(tempWeight);

        if (tempWeight < 1 || tempWeight > 100) {
            setError("Weight must be positive number and less than 100.");
            handleEditClose();
            return;
        }


        // const sumOfWeights = checkWeightSum();
        // if (sumOfWeights !== 100) {
        //     setError("Sum Of Weight must be 100.");
        //     handleEditClose();
        //     return;
        // }


        const response = await editRowByDimensionName(tempDimension, tempWeight);
        console.log("response of put mtd", response)
        if (!response.valid) {
            setError(response.message);
            handleEditClose();
            return;
        }


        setError(response.message);
        setTableData(prevState => {
            const newState = prevState.map(item => {
                if (item.dimension === tempDimension) {
                    // Create a new object to avoid modifying the original object
                    return {
                        ...item,
                        weight: parseInt(tempWeight)
                    };
                }
                return item; // Return the original object for other items
            });
            return newState; // Return the new state to update the state variable
        });

        console.log(tableData)
        handleEditClose();
    }

    const checkWeightSum = (tempDimension, tempWeight) => {
        console.log("calculating weight")
        const sumOfWeights = tableData && tableData.reduce((sum, data) => {
            if (data.dimension === tempDimension) {
                return sum + tempWeight;
            } else {
                return sum + data.weight;
            }
        }, 0);

        // Step 3: Call setTotalWeight to update the totalWeight state
        setTotalWeight(sumOfWeights);

        if (sumOfWeights > 100 || sumOfWeights < 0) {
            settotalWeightError('Sum Of Weights is either less than 0  OR greater than 100%')
        }
    };

    const [showAlert, setShowAlert] = useState(true);



    return (
        <>
            <div className='rd-dashboard'>

                <div>
                    <h3>Risk Dimension Dashboard: Edit & Delete</h3>
                </div>

                {
                    <Alert severity="info">Change all Dimension's weight whose value is 0(zero) ,
                        Make sure that Sum of Dimensio's' weight is 100%
                    </Alert>

                }

                {
                    showAlert &&
                    <Alert severity="info" onClose={() => { setShowAlert(!showAlert) }}>These values are automatically filled, If you dont see your dimension Kindly Reload!</Alert>
                }
                {error &&
                    <Alert severity="error">{error}</Alert>
                }
                {totalWeightError &&
                    <Alert severity="error">{totalWeightError}</Alert>
                }
                <div className='table-container'>
                    <TableContainer component={Paper} >
                        <Table sx={{ minWidth: 650 }} size='small' aria-label="caption table">
                            <caption>Total Weight {totalWeight}</caption>

                            <TableHead>
                                <TableRow>
                                    <TableCell>Dimension</TableCell>
                                    <TableCell>Weight</TableCell>

                                </TableRow>
                            </TableHead>
                            <TableBody>
                                {tableData && tableData.map((row, index) => {
                                    const { dimension, weight } = row;
                                    return (

                                        <TableRow
                                            key={index}
                                            sx={{ '&:last-child td, &:last-child th': { border: 0 } }}>
                                            <TableCell component="th" scope="row">
                                                {dimension}
                                            </TableCell>
                                            <TableCell component="th" scope="row">
                                                {weight}
                                            </TableCell>

                                            <TableCell>
                                                <Tooltip TransitionComponent={Fade} TransitionProps={{ timeout: 600 }} title="Delete" placement="left" >
                                                    <IconButton ref={iconButtonRef} onClick={() => handleDeleteClickOpen(dimension)}>
                                                        <DeleteIcon />
                                                    </IconButton>
                                                </Tooltip>
                                                <Dialog open={open} onClose={handleDeleteClose} aria-labelledby="alert-dialog-title" aria-describedby="alert-dialog-description">
                                                    <DialogTitle id="alert-dialog-title">{"Confirm Delete?"}</DialogTitle>
                                                    <DialogContent>
                                                        <DialogContentText id="alert-dialog-description">
                                                            You won't be able to retrive this data.
                                                            sure you want to delete it?
                                                        </DialogContentText>
                                                    </DialogContent>
                                                    <DialogActions>
                                                        <Button onClick={handleDeleteClose}>close</Button>
                                                        <Button onClick={handleDeleteClick} autoFocus>
                                                            DELETE
                                                        </Button>
                                                    </DialogActions>
                                                </Dialog>
                                            </TableCell>



                                            < TableCell >
                                                <Tooltip TransitionComponent={Fade}
                                                    TransitionProps={{ timeout: 600 }} title="Edit" placement="right">
                                                    <IconButton onClick={() => handleEditClickOpen(dimension, weight)}>
                                                        <EditIcon />
                                                    </IconButton>
                                                </Tooltip>

                                                <Dialog open={editOpen} onClose={handleEditClose}>
                                                    <DialogTitle>Edit Dimension's weight</DialogTitle>
                                                    <DialogContent>
                                                        <Grid container spacing={2}>
                                                            <Grid item xs={5}>
                                                                <TextField
                                                                    margin="dense"
                                                                    id="dimension"
                                                                    label="Dimension Name"
                                                                    fullWidth
                                                                    defaultValue={tempDimension}
                                                                    disabled
                                                                />
                                                            </Grid>
                                                            <Grid item xs={7}>
                                                                <TextField
                                                                    margin="dense"
                                                                    id="weight"
                                                                    label="Dimension Weight"
                                                                    type="number"
                                                                    fullWidth
                                                                    required
                                                                    value={tempWeight}
                                                                    onChange={handleWeightChange}
                                                                    // input can be from 1-100
                                                                    inputProps={{ inputMode: 'numeric', pattern: '^([1-9]|[1-9][0-9]|100)$' }}
                                                                    error={error !== ''}
                                                                // helperText
                                                                />
                                                            </Grid>
                                                        </Grid>
                                                    </DialogContent>
                                                    <DialogActions>
                                                        <Button onClick={handleEditClose}>Cancel</Button>
                                                        <Button onClick={handleEditClick}>Edit</Button>
                                                    </DialogActions>
                                                </Dialog>
                                            </TableCell>
                                        </TableRow>
                                    )
                                })}
                            </TableBody>
                        </Table >
                    </TableContainer >

                </div >
            </div >
        </>
    );
}
