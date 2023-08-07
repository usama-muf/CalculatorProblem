import { Button, Dialog, DialogActions, DialogContent, DialogContentText, DialogTitle, Fade, IconButton, Paper, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Tooltip } from '@mui/material';
import React, { useEffect, useRef, useState } from 'react'
import DeleteIcon from '@mui/icons-material/Delete';
import EditIcon from '@mui/icons-material/BorderColorTwoTone';

import './CrsDashboard.css'
import { Link } from 'react-router-dom';

export default function CrsDashboard() {
    const [tableData, setTableData] = useState([]);
    const [headers, setHeader] = useState([]);
    const iconButtonRef = useRef(null);
    const [open, setOpen] = React.useState(false);
    const [elementToDelete, setElementToDelete] = useState('');


    useEffect(() => {
        const fetchData = async () => {
            try {
                const header = await fetch('http://localhost:8092/v1/api/crs/findUniqueParamsName');
                const response = await fetch('http://localhost:8092/v1/api/crs/getall');

                const headerData = await header.json();
                const responseData = await response.json();
                console.log(responseData)


                // Check if responseData is an array
                if (Array.isArray(responseData)) {
                    setTableData(responseData);
                } else {
                    console.error('Invalid response format:', responseData);
                    setTableData([]);
                }
                if (Array.isArray(headerData)) {
                    setHeader(headerData);
                } else {
                    console.error('Invalid response format:', headerData);
                    setHeader([]);
                }
            } catch (error) {
                console.error('Error fetching data:', error);
                setTableData([]);
                setHeader([]);
            }
        };

        fetchData();
    }, []);

    // Generate the table header using the company names and parameter names
    const header =
        headers.length > 0
            ? ['CompanyName', ...headers]
            : [];


    const deleteRowByCompanyName = async (companyName) => {
        try {
            const response = await fetch(`http://localhost:8092/v1/api/crs/delete/${companyName}`, {
                method: 'DELETE',
            });

            if (response.ok) {
                // Request was successful
                console.log('DELETE request was successful');
                const updatedTableData = tableData.filter((row) => row.companyName !== companyName);
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

    const handleClickOpen = (companyName) => {
        setOpen(true);
        setElementToDelete(companyName)
    };

    const handleClose = () => {
        setOpen(false);
        setElementToDelete('');
    };

    const handleDeleteClick = async () => {
        console.log(`Deleting ${elementToDelete}`);
        deleteRowByCompanyName(elementToDelete);
        handleClose();
    };

    return (
        <>
            < div className='btn-container' >
                <h3>Company Dimension Dashboard</h3>
            </div>

            < div className='btn-container' >
                <Link to='/newcrsdash'>
                    <Button className='button' variant="outlined" color="primary" >
                        Create New Company with Dimensions
                    </Button>
                </Link>
            </div>
            <div className='table-container'>
                <TableContainer component={Paper} >
                    <Table sx={{ minWidth: 650 }} size='small' aria-label="a dense table">
                        <TableHead>
                            <TableRow>
                                {header.map((column, index) =>
                                    <TableCell key={index} style={{ fontWeight: 'bold' }}>{column}</TableCell>
                                )}
                            </TableRow>
                        </TableHead>
                        <TableBody>
                            {tableData.map((row, index) => (
                                <TableRow
                                    key={index}
                                    sx={{ '&:last-child td, &:last-child th': { border: 0 } }}>
                                    <TableCell component="th" scope="row">
                                        {row.companyName}
                                    </TableCell>


                                    {headers.map(header => {
                                        const matchingData = row.parameters.find(data => data.parameterName === header);
                                        const value = matchingData ? matchingData.parameterValue : '-'; // Retrieve value if matching header exists, otherwise empty string
                                        return <TableCell align='center' key={header}>{value}</TableCell>
                                    })}

                                    {/* <DeleteCell companyName={row.companyName} /> */}
                                    <TableCell>
                                        <Tooltip TransitionComponent={Fade} TransitionProps={{ timeout: 600 }} title="Delete" placement="left" >
                                            <IconButton ref={iconButtonRef} onClick={() => handleClickOpen(row.companyName)}>
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
                                                <Link to={`/crsdash/edit/${row.companyName}`}>
                                                    <EditIcon />
                                                </Link>
                                            </IconButton>
                                        </Tooltip></TableCell>
                                </TableRow>
                            ))}
                        </TableBody>
                    </Table>
                </TableContainer>
            </div>
        </>
    );

}
