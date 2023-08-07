import React, { useEffect, useRef, useState } from 'react'
import useCustomFunction from '../small-components/useCustomFunction';
import { Alert, Box, Button, Dialog, DialogActions, DialogContent, DialogContentText, DialogTitle, Fade, IconButton, Paper, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Tooltip } from '@mui/material';
import DeleteIcon from '@mui/icons-material/Delete';
import EditIcon from '@mui/icons-material/BorderColorTwoTone';
import { Link } from 'react-router-dom';

export default function ScDashboard() {

    const { handleGetAllScoreCapData, handleDeleteRequestSc } = useCustomFunction();

    const [scoreCapData, setScoreCapData] = useState([]);
    const iconButtonRef = useRef(null);
    const [open, setOpen] = React.useState(false);
    const [elementToDelete, setElementToDelete] = useState('');
    const [errorMessage, setErrorMessage] = useState('')
    const [alertSeverity, setAlertSeverity] = useState('')
    useEffect(() => {
        const fetchScoreLevels = async () => {
            const data = await handleGetAllScoreCapData();
            setScoreCapData(data);
        };

        fetchScoreLevels();
    }, []);

    const handleClickOpen = (id) => {
        setOpen(true);
        setElementToDelete(id)
    };

    const handleClose = () => {
        setOpen(false);
        setElementToDelete('');
    };

    const handleDeleteClick = async () => {
        console.log(`Deleting ${elementToDelete}`);
        const response = await handleDeleteRequestSc(elementToDelete);
        console.log(response);

        handleClose();
        setAlertSeverity(response.alertSeverity);
        setErrorMessage(response.message);


        if (response.alertSeverity === 'success') {
            const updatedScoreLevels = scoreCapData.filter(
                (row) => row.id !== elementToDelete
            );
            setScoreCapData(updatedScoreLevels);

            setTimeout(() => {
                setErrorMessage('');
            }, 2500);
        }


    };

    return (
        <div className='sc-container'>
            <div className='btn-container'>
                <h3>Score Cap Dashboard</h3>
            </div>
            < div className='btn-container' >
                <Link to='/newscdash'>
                    <Button className='button' variant="outlined" color="primary" >
                        Add New Score Cap Data
                    </Button>
                </Link>
            </div>
            <div className='table-container'>
                <TableContainer component={Paper} >
                    <Table sx={{ minWidth: 650 }} size='small' aria-label="a dense table">
                        <TableHead>
                            <TableRow>
                                <TableCell>Score Cap Condition</TableCell>
                                <TableCell>Count</TableCell>
                                <TableCell>Value</TableCell>
                            </TableRow>
                        </TableHead>
                        <TableBody>
                            {scoreCapData.map((row, index) => (
                                <TableRow
                                    key={index}
                                    sx={{ '&:last-child td, &:last-child th': { border: 0 } }}>
                                    <TableCell component="th" scope="row">
                                        {row.scoreCapCondition}
                                    </TableCell>
                                    <TableCell>
                                        {row.countCondition}
                                    </TableCell>
                                    <TableCell>
                                        {row.totalRiskCappedScore}
                                    </TableCell>

                                    <TableCell>
                                        <Tooltip TransitionComponent={Fade} TransitionProps={{ timeout: 600 }} title="Delete" placement="left" >
                                            <IconButton ref={iconButtonRef} onClick={() => handleClickOpen(row.id)}>
                                                <DeleteIcon />
                                            </IconButton>
                                        </Tooltip>
                                        <Dialog open={open} onClose={handleClose} aria-labelledby="alert-dialog-title" aria-describedby="alert-dialog-description">
                                            <DialogTitle id="alert-dialog-title">{"Confirm Delete?"}</DialogTitle>
                                            <DialogContent>
                                                <DialogContentText id="alert-dialog-description">
                                                    You won't be able to retrive this data.
                                                    sure you want to delete it?
                                                </DialogContentText>
                                            </DialogContent>
                                            <DialogActions>
                                                <Button onClick={handleClose}>close</Button>
                                                <Button onClick={handleDeleteClick} autoFocus>
                                                    DELETE
                                                </Button>
                                            </DialogActions>
                                        </Dialog>
                                    </TableCell>



                                    {/* <TableCell  onClick={() => deleteReqCrsDash(row.companyName)} title='Delete' >🗑️</TableCell> */}
                                    <TableCell>
                                        <Tooltip TransitionComponent={Fade}
                                            TransitionProps={{ timeout: 600 }} title="Edit" placement="right">
                                            <IconButton >
                                                <Link to={`/scdash/edit/${row.id}`}>
                                                    <EditIcon />
                                                </Link>
                                            </IconButton>
                                        </Tooltip>
                                    </TableCell>
                                </TableRow>
                            ))}
                        </TableBody>
                    </Table>
                </TableContainer>
            </div>
            {errorMessage && (
                <Box display="flex" justifyContent="center" mt={2}>
                    <Alert severity={alertSeverity}>{errorMessage}</Alert>
                </Box>
            )}

        </div>

    )
}