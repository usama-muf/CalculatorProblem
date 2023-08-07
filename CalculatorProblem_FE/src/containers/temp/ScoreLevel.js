import React, { useEffect, useRef, useState } from 'react'
import useCustomFunction from '../small-components/useCustomFunction';
import { Alert, Box, Button, Dialog, DialogActions, DialogContent, DialogContentText, DialogTitle, Fade, IconButton, Paper, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Tooltip } from '@mui/material';
import DeleteIcon from '@mui/icons-material/Delete';
import EditIcon from '@mui/icons-material/BorderColorTwoTone';
import { Link } from 'react-router-dom';

export default function ScoreLevel() {

    const { handleGetAllRiskScoreLevel, handleDeleteRequestRsl } = useCustomFunction();

    const [scoreLevels, setScoreLevels] = useState([]);
    const iconButtonRef = useRef(null);
    const [open, setOpen] = React.useState(false);
    const [elementToDelete, setElementToDelete] = useState('');
    const [errorMessage, setErrorMessage] = useState('')
    const [alertSeverity, setAlertSeverity] = useState('')
    useEffect(() => {
        const fetchScoreLevels = async () => {
            const data = await handleGetAllRiskScoreLevel();
            setScoreLevels(data);
        };

        fetchScoreLevels();
    }, []);

    const handleClickOpen = (levelName) => {
        setOpen(true);
        setElementToDelete(levelName)
    };

    const handleClose = () => {
        setOpen(false);
        setElementToDelete('');
    };

    const handleDeleteClick = async () => {
        console.log(`Deleting ${elementToDelete}`);
        const response = await handleDeleteRequestRsl(elementToDelete);
        console.log(response);

        handleClose();
        setAlertSeverity(response.alertSeverity);
        setErrorMessage(response.message);


        if (response.alertSeverity === 'success') {
            const updatedScoreLevels = scoreLevels.filter(
                (row) => row.level !== elementToDelete
            );
            setScoreLevels(updatedScoreLevels);

        }


    };

    return (
        <>
            <div className='btn-container'>
                <h3>Risk Score Level Dashboard</h3>
            </div>
            < div className='btn-container' >
                <Link to='/newrsldash'>
                    <Button className='button' variant="outlined" color="primary" >
                        Add New Risk Score Levels
                    </Button>
                </Link>
            </div>
            <div className='table-container'>
                <TableContainer component={Paper} >
                    <Table sx={{ minWidth: 650 }} size='small' aria-label="a dense table">
                        <TableHead>
                            <TableRow>
                                <TableCell>Level Name</TableCell>
                                <TableCell>Group</TableCell>
                            </TableRow>
                        </TableHead>
                        <TableBody>
                            {scoreLevels.map((row, index) => (
                                <TableRow
                                    key={index}
                                    sx={{ '&:last-child td, &:last-child th': { border: 0 } }}>
                                    <TableCell component="th" scope="row">
                                        {row.level}
                                    </TableCell>
                                    <TableCell>
                                        {row.minScore}-{row.maxScore}
                                    </TableCell>

                                    <TableCell>
                                        <Tooltip TransitionComponent={Fade} TransitionProps={{ timeout: 600 }} title="Delete" placement="left" >
                                            <IconButton ref={iconButtonRef} onClick={() => handleClickOpen(row.level)}>
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
                                                <Link to={`/rsldash/edit/${row.level}`}>
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

        </>

    )
}
