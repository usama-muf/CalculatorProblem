// import { Grid, Skeleton, TextField } from '@mui/material';
// import React, { useEffect, useState } from 'react'
// import { useParams } from 'react-router-dom'
// // import AddIcon from '@mui/icons-material/Add'
// import AddNewParameterDialog from '../small-components/AddNewParameterDialog';
// import useCustomFunction from '../small-components/useCustomFunction'
// import { Button } from '@material-ui/core';


// export default function EditRslDashboard() {
//     const { levelName } = useParams();
//     const [rowData, setRowData] = useState(null);
//     const [parameterMap, setParameterMap] = useState(new Map());
//     const { handlePutRequestCrs } = useCustomFunction();

//     const [minScore, setMinScore] = useState();
//     const [maxScore, setMaxScore] = useState();


//     useEffect(() => {
//         const fetchData = async () => {
//             try {
//                 const response = await fetch(`http://localhost:8092/v1/api/rsl/get/${levelName}`);
//                 const responseData = await response.json();
//                 setRowData(responseData);
//                 console.log(responseData)
//             } catch (error) {
//                 console.error('Error fetching row data:', error);
//             }
//         };

//         fetchData();
//     }, [levelName]);


//     console.log(rowData)


//     // useEffect(() => {

//     //     const setParameterMapFn = () => {
//     //         console.log("Inside useEffect setParameterMapFn");
//     //         const initialMap = new Map();
//     //         rowData.parameters.forEach((parameter) => {
//     //             initialMap.set(parameter.parameterName, parameter.parameterValue);
//     //         });
//     //         setParameterMap(initialMap);
//     //     };

//     //     if (rowData) {
//     //         setParameterMapFn();
//     //     }
//     // }, [rowData, setParameterMap]);

//     // const handleContent = async (e, parameterName) => {

//     //     const { value } = e.target;
//     //     setParameterMap(prevState => {
//     //         const newState = new Map(prevState);
//     //         newState.set(parameterName, value);
//     //         return newState;
//     //     });
//     // };


//     if (!rowData) {
//         // Render loading state or fallback UI while fetching the row data
//         return (

//             <div  >
//                 <Skeleton animation="wave" variant="text" />
//                 <Skeleton animation="wave" variant="text" />
//                 <Skeleton animation="wave" variant="text" />
//             </div>
//         );
//     }


//     const handleMinScoreChange = (e) => {
//         setMinScore(e.target.value);
//     };

//     const handleMaxScoreChange = (e) => {
//         setMaxScore(e.target.value);
//     };

//     const handleSave = () => {
//         data = ({ ...data, minScore: parseInt(minScore), maxScore: parseInt(maxScore) });
//     };

//     return (
//         <Grid container spacing={2}>
//             <Grid item xs={12}>
//                 <TextField
//                     label="Minimum Score"
//                     type="number"
//                     required
//                     value={minScore}
//                     onChange={handleMinScoreChange}
//                 />
//             </Grid>
//             <Grid item xs={12}>
//                 <TextField
//                     label="Maximum Score"
//                     type="number"
//                     required
//                     value={maxScore}
//                     onChange={handleMaxScoreChange}
//                 />
//             </Grid>
//             <Grid item xs={12}>
//                 <Button variant="contained" color="primary" onClick={handleSave}>
//                     Save
//                 </Button>
//             </Grid>
//         </Grid>
//     );
// }

import React, { useEffect, useState } from 'react';
import { Grid, TextField, Button, Skeleton, Box, Alert } from '@mui/material';
import { useParams } from 'react-router';
import useCustomFunction from '../small-components/useCustomFunction';
import { useNavigate } from 'react-router-dom';

const EditRslDashboard = () => {
    const { levelName } = useParams();
    const [data, setData] = useState(null);
    const [minScore, setMinScore] = useState('');
    const [maxScore, setMaxScore] = useState('');
    const [error, setError] = useState('');
    const [errorMessage, setErrorMessage] = useState('')
    const navigate = useNavigate();

    const { handlePutRequestRsl } = useCustomFunction();



    useEffect(() => {
        const fetchData = async () => {
            try {
                const response = await fetch(`http://localhost:8092/v1/api/rsl/get/${levelName}`);
                const responseData = await response.json();
                setData(responseData);

                // Assuming the API response contains properties minScore and maxScore, you can set them in the component's state.
                if (responseData && responseData.minScore !== undefined && responseData.maxScore !== undefined) {
                    setMinScore(responseData.minScore.toString());
                    setMaxScore(responseData.maxScore.toString());
                }
                console.log(responseData)
            } catch (error) {
                console.error('Error fetching row data:', error);
            }
        };

        fetchData();
    }, [levelName]);


    const handleMinScoreChange = (e) => {
        setMinScore(e.target.value);
    };

    const handleMaxScoreChange = (e) => {
        setMaxScore(e.target.value);
    };

    const handleSubmit = async () => {
        const parsedMinScore = parseInt(minScore);
        const parsedMaxScore = parseInt(maxScore);

        if (Number.isNaN(parsedMinScore) || Number.isNaN(parsedMaxScore)) {
            setError('Please enter valid numeric values for minScore and maxScore.');
            return;
        }

        if (parsedMinScore < 0 || parsedMinScore > 100 || parsedMaxScore < 0 || parsedMaxScore > 100) {
            setError('minScore and maxScore must be between 0 and 100.');
            return;
        }

        if (parsedMinScore > parsedMaxScore) {
            setError('minScore must be less than maxScore.');
            return;
        }

        setData({ ...data, minScore: parsedMinScore, maxScore: parsedMaxScore });



        const response = await handlePutRequestRsl(data.level, { minScore: parsedMinScore, maxScore: parsedMaxScore });

        if (response.interfering) {
            setErrorMessage(response.message);
        }

        else {
            // Navigate to the <ScoreLevel> component
            navigate('/rsldash', { replace: true });


        }



        console.log(response)
    };
    useEffect(() => {
        // This useEffect will be triggered whenever 'data' is updated.
        console.log(data);
    }, [data]);


    if (!data) {
        // Render loading state or fallback UI while fetching the row data
        return (

            <div  >
                <Skeleton animation="wave" variant="text" />
                <Skeleton animation="wave" variant="text" />
                <Skeleton animation="wave" variant="text" />
            </div>
        );
    }


    return (
        <div>
            <div>
                <h3>Risk Score Level Dashboard : Edit</h3>
            </div>
            <TextField
                id="outlined-read-only-input"
                // defaultValue={data && data.level}
                value={data && data.level}
                fullWidth
                InputProps={{
                    readOnly: true,
                    style: {
                        fontSize: '20px',
                        fontWeight: 'bold',
                        fontFamily: 'sans-serif',
                        marginBottom: '2vh'
                    }
                }}
                margin='dense'
            />

            <Grid container spacing={2}>
                <Grid item xs={12}>
                    <TextField
                        label="Minimum Score"
                        type="number"
                        required
                        value={minScore}
                        onChange={handleMinScoreChange}
                        error={!!error}
                        helperText={error}
                    />
                </Grid>
                <Grid item xs={12}>
                    <TextField
                        label="Maximum Score"
                        type="number"
                        required
                        value={maxScore}
                        onChange={handleMaxScoreChange}
                        error={!!error}
                        helperText={error}
                    />
                </Grid>
                <Grid item xs={12}>
                    <Button variant="contained" color="primary" onClick={handleSubmit}>
                        Save
                    </Button>
                </Grid>
            </Grid>

            {errorMessage && (
                <Box display="flex" justifyContent="center" mt={2}>
                    <Alert severity='error'>{errorMessage}</Alert>
                </Box>
            )}

        </div>
    );
};

export default EditRslDashboard;
