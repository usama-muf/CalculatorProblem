import React, { useRef, useState } from 'react';
import TableCell from '@material-ui/core/TableCell';
import Tooltip from '@material-ui/core/Tooltip';
import IconButton from '@material-ui/core/IconButton';
// import DeleteIcon from '@material-ui/icons/Delete';
import DeleteIcon from '@mui/icons-material/Delete';

import Dialog from '@material-ui/core/Dialog';
import DialogTitle from '@material-ui/core/DialogTitle';
import DialogContent from '@material-ui/core/DialogContent';
import DialogContentText from '@material-ui/core/DialogContentText';
import DialogActions from '@material-ui/core/DialogActions';
import Button from '@material-ui/core/Button';
import { Fade } from '@mui/material';

function DeleteCell({ companyName }) {
    const [open, setOpen] = useState(false);
    const iconButtonRef = useRef(null);

    const handleClickOpen = () => {
        console.log("handling click open")
        setOpen(true);
    };

    const handleClose = () => {
        setOpen(false);
    };

    const handleDeleteClick = () => {
        // Implement your delete functionality here
        console.log(`Deleting ${companyName}`);
        handleClose();
    };

    return (
        <TableCell>
            <Tooltip TransitionComponent={Fade} TransitionProps={{ timeout: 600 }} title="Delete" placement="left" >
                <IconButton ref={iconButtonRef} onClick={handleClickOpen}>
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
    );
}

export default DeleteCell;
