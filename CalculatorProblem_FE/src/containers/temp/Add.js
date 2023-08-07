import { Button, Dialog, DialogContent, DialogTitle } from '@mui/material'
import React, { useState } from 'react'
import { Link } from 'react-router-dom'
import HorizontalLinearStepper from './stepper'

export default function Add() {

    // const [isSubmitSuccessful, setIsSubmitSuccessful] = useState(false);
    const [isDialogOpen, setIsDialogOpen] = useState(true);

    const handleCloseDialog = () => {
        setIsDialogOpen(false);
    }



    return (
        <div>

            <div className='btn-container'>
                <Link to='/newcrsdash'>
                    <Button className='button' variant="outlined" color="primary" >
                        Add to Company Risk Score Table
                    </Button>
                </Link>

                <Link to='/rddash'>
                    <Button className='button' variant="outlined" color="primary">
                        Update Risk Dimension Table
                    </Button>
                </Link>

                <Link to='/newrcldash'>
                    <Button className='button' variant="outlined" color="primary">
                        Add Risk Calculation Logic Table
                    </Button>
                </Link>


            </div>


            <Dialog open={isDialogOpen} onClose={handleCloseDialog} fullWidth={true} maxWidth={'xl'}>
                <DialogTitle>Stepper Dialog</DialogTitle>
                <DialogContent>
                    {<HorizontalLinearStepper currentStepLvl={0} />}
                </DialogContent>
            </Dialog>
        </div>

    )
}
