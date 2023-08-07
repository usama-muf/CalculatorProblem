import { Alert, Button, Dialog, DialogActions, DialogContent, DialogContentText, DialogTitle, Fab, Fade, Grid, IconButton, Paper, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, TextField, Tooltip } from '@mui/material';
import React, { useEffect, useRef, useState } from 'react'
import DeleteIcon from '@mui/icons-material/Delete';
import EditIcon from '@mui/icons-material/BorderColorTwoTone';
import AddIcon from '@mui/icons-material/Add'
import InfoIcon from '@mui/icons-material/Info';


import './RclDashboard.css'
import useCustomFunction from '../small-components/useCustomFunction';
import AddNewDialogBox from '../small-components/AddNewDialogBox';
import { Link } from 'react-router-dom';


export default function RclDashboard() {

    const [tableData, setTableData] = useState([]);
    const iconButtonRef = useRef(null);
    const [open, setOpen] = React.useState(false);
    const [editOpen, setEditOpen] = React.useState(false);
    const [formula, setFormula] = useState('')
    const [elementToDelete, setElementToDelete] = useState('')
    const [elementName, setElementName] = useState('')
    const [error, setError] = useState('');
    const [textBoxError, setTextBoxError] = useState('')
    const [dialogueOpen, setDialogOpen] = useState(false)

    const { handlePutRequestRcl, handlePostRequestRcl } = useCustomFunction();

    useEffect(() => {
        const fetchData = async () => {
            try {
                // const header = await fetch('http://localhost:8092/v1/api/rcl/getall');
                const response = await fetch('http://localhost:8092/v1/api/rcl/getall');
                const responseCore = await fetch('http://localhost:8092/v1/api/rcl/getall/core');

                // const headerData = await header.json();
                const responseData = await response.json();
                const responseDataCore = await responseCore.json();

                // Check if responseData is an array
                if (!Array.isArray(responseData)) {
                    console.error('Invalid response format:', responseData);
                    setTableData([]);
                }
                if (!Array.isArray(responseDataCore)) {
                    console.error('Invalid response format:', responseDataCore);
                    setTableData([]);
                }
                else {
                    setTableData(responseData);
                }
            } catch (error) {
                console.error('Error fetching data:', error);
                setTableData([]);
                // setHeader([]);
            }
        };

        fetchData();
    }, []);

    const deleteRowByElementName = async (elementName) => {
        try {
            const response = await fetch(`http://localhost:8092/v1/api/rcl/delete/${elementName}`, {
                method: 'DELETE',
            });

            if (response.ok) {
                // Request was successful
                console.log('DELETE request was successful');
                const updatedTableData = tableData.filter((row) => row.elementName !== elementName);
                setTableData(updatedTableData);
                // fetchData();

            } else {
                // Request failed
                console.error('DELETE request failed');
            }
        } catch (error) {
            console.error('An error occurred:', error);
        }
    };



    const handleDeleteClickOpen = (elementName) => {
        setOpen(true);
        setElementToDelete(elementName);
    };

    const handleDeleteClose = () => {
        setOpen(false);
        setElementToDelete('');
    };

    const handleDeleteClick = async () => {
        console.log(`Deleting ${elementToDelete}`);
        deleteRowByElementName(elementToDelete);
        handleDeleteClose();
    };

    const handleEditClickOpen = (elementName, formula) => {
        setEditOpen(true)
        setElementName(elementName);
        setFormula(formula)
    };

    const handleEditClose = () => {
        setEditOpen(false)
        setElementName('');
        setFormula('');
        setTextBoxError('');
    };

    const handleFormulaChange = (event) => {
        setTextBoxError('');
        setFormula(event.target.value)
    };

    const handleEditClick = async () => {
        if (formula.length < 5) {
            setTextBoxError("Invalid formula length !")
            return;
        }

        // handle updation in Risk Calc Logic table.
        const response = await handlePutRequestRcl(elementName, formula);
        if (!response.valid) {
            setTextBoxError(response.message);
            return;
        }

        updateTalbeData();
        handleEditClose();
    }
    // console.log(tableData)

    const updateTalbeData = () => {
        // Iterate over the tableData and change the formula value to 'value'
        const updatedData = tableData.map((data) =>
            data.elementName === elementName ? { ...data, formula: formula } : data
        );

        // Update the state with the updatedData
        setTableData(updatedData);
    };

    return (
        <>
            <div className='rcl-dashboard'>

                < div className='btn-container' >
                    <h3>Risk Calculation Logic Dashboard: Edit & Delete</h3>
                </div>
                < div className='btn-container' >
                    <Link to='/newrcldash'>
                        <Button className='button' variant="outlined" color="primary" >
                            Add New Formula
                        </Button>
                    </Link>
                </div>

                {error &&
                    <Alert severity="error">{error}</Alert>
                }
                <div className='table-container'>
                    <TableContainer component={Paper} >
                        <Table sx={{ minWidth: 650 }} size='small' aria-label="a dense table">
                            <TableHead>
                                <TableRow>
                                    <TableCell > Element Name</TableCell>
                                    <TableCell >Formula</TableCell>
                                </TableRow>
                            </TableHead>
                            <TableBody>
                                {tableData.map((row, index) => (

                                    < TableRow key={index} >
                                        <TableCell >
                                            {row.elementName}
                                        </TableCell>

                                        <TableCell >
                                            {row.formula}
                                        </TableCell>
                                        <TableCell>
                                            <Tooltip TransitionComponent={Fade} TransitionProps={{ timeout: 600 }} title="Delete" placement="left" >
                                                <IconButton ref={iconButtonRef} onClick={() => handleDeleteClickOpen(row.elementName)}>
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
                                                <IconButton onClick={() => handleEditClickOpen(row.elementName, row.formula)}>

                                                    <EditIcon />
                                                </IconButton>
                                            </Tooltip>
                                            <Dialog open={editOpen} onClose={handleEditClose}>
                                                <DialogTitle>Edit Formula</DialogTitle>
                                                <DialogContent>
                                                    <Grid container spacing={2}>
                                                        <Grid item xs={5}>
                                                            <TextField
                                                                margin="dense"
                                                                id="elementName"
                                                                label="Element Name"
                                                                fullWidth
                                                                defaultValue={elementName}
                                                                disabled
                                                            />
                                                        </Grid>
                                                        <Grid item xs={7}>
                                                            <TextField

                                                                margin="dense"
                                                                id="formula"
                                                                label="Formula"
                                                                type="text"
                                                                focused
                                                                fullWidth
                                                                required
                                                                defaultValue={formula}
                                                                error={!!textBoxError}
                                                                helperText={textBoxError}
                                                                onChange={handleFormulaChange}
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

                                ))}
                            </TableBody>
                        </Table>
                    </TableContainer >

                    <br />

                    {/* <TableContainer component={Paper} >
                        <Table sx={{ minWidth: 650 }} size='small' aria-label="a dense table">

                            <TableBody>
                                {tableDataCore.map((row, index) => (

                                    < TableRow key={index} >
                                        <TableCell >
                                            {row.elementName}
                                        </TableCell>

                                        <TableCell >
                                            {row.formula}
                                        </TableCell>
                                        <TableCell>
                                            <Tooltip TransitionComponent={Fade} TransitionProps={{ timeout: 600 }} title="Delete" placement="left" >
                                                <IconButton ref={iconButtonRef} onClick={() => handleDeleteClickOpen(row.elementName)}>
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
                                                    <Button onClick={handleDeleteClickCore} autoFocus>
                                                        DELETE
                                                    </Button>
                                                </DialogActions>
                                            </Dialog>
                                        </TableCell>

                                        < TableCell >
                                            <Tooltip TransitionComponent={Fade}
                                                TransitionProps={{ timeout: 600 }} title="Edit" placement="right">
                                                <IconButton onClick={() => handleEditClickOpenCore(row.elementName, row.formula)}>

                                                    <EditIcon />
                                                </IconButton>
                                            </Tooltip>
                                            <Dialog open={editOpenCore} onClose={handleEditCloseCore}>
                                                <DialogTitle>Edit Formula</DialogTitle>
                                                <DialogContent>
                                                    <Grid container spacing={2}>
                                                        <Grid item xs={5}>
                                                            <TextField
                                                                margin="dense"
                                                                id="elementName"
                                                                label="Element Name"
                                                                fullWidth
                                                                defaultValue={elementName}
                                                                disabled
                                                            />
                                                        </Grid>
                                                        <Grid item xs={7}>
                                                            <TextField
                                                                margin="dense"
                                                                id="formula"
                                                                label="Formula"
                                                                type="text"
                                                                fullWidth

                                                                required
                                                                defaultValue={formula}
                                                                // error={valueError !== ''}
                                                                // value={formula}
                                                                onChange={handleFormulaChangeCore}
                                                            />
                                                        </Grid>
                                                    </Grid>
                                                </DialogContent>
                                                <DialogActions>
                                                    <Button onClick={handleEditCloseCore}>Cancel</Button>
                                                    <Button onClick={handleEditClickCore}>Edit</Button>
                                                </DialogActions>
                                            </Dialog>
                                        </TableCell>
                                    </TableRow>

                                ))}
                            </TableBody>
                        </Table>
                    </TableContainer > */}

                    {/* <Tooltip TransitionComponent={Fade} TransitionProps={{ timeout: 600 }} title="Add new Risk Calculation Logic" placement="left" >
                        <Fab style={{ margin: '2vh', float: 'right' }} color="primary" aria-label="add" onClick={handleOpenDialog}>
                            <AddIcon />
                        </Fab>
                    </Tooltip> */}


                    {/* code to add new values in Risk_Calc_Logic table*/}
                    {/* <AddNewDialogBox
                        heading={"Add New Risk Calculation Logic"}
                        open={isDialogOpen}
                        onClose={handleCloseDialog}
                        onParameterSubmit={handleParameterSubmit}
                    /> */}
                </div >

            </div >
        </>
    );
}
