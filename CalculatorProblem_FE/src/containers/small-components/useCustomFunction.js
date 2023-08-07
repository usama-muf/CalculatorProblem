
const useCustomFunction = () => {

    const baseUrl = "http://localhost:8092/v1/api";

    const handlePutRequestCrs = async (companyName, parameterMap) => {


        const parameters = Object.fromEntries(parameterMap);

        const data = {
            parameters,
        };


        console.log("Inside put request sending this data to updation", JSON.stringify(data));
        const url = `http://localhost:8092/v1/api/crs/update/${encodeURIComponent(companyName)}`;

        try {
            const response = await fetch(url, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                },
                // body: JSON.stringify(parameterMap),
                body: JSON.stringify(data),
            });

            if (!response.ok) {
                throw new Error('Request failed');
            }

            // Handle successful response
            console.log('PUT request successful');
        } catch (error) {
            // Handle error
            console.error('Error:', error.message);
        }
        // return handlePutRequestCrs;
    }


    const handlePutRequestRcl = async (elementName, formula) => {

        // const parameters = Object.fromEntries(parameterMap);

        const data = {
            "elementName": elementName.trim(),
            "formula": formula.trim()
        };

        console.log(elementName, formula)

        console.log("Inside put request sending this data to updation", JSON.stringify(data));
        const url = `http://localhost:8092/v1/api/rcl/update/${encodeURIComponent(elementName)}`;

        try {
            const response = await fetch(url, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                },
                // body: JSON.stringify(parameterMap),
                body: JSON.stringify(data),
            });

            if (!response.ok) {
                throw new Error('Request failed');
            }

            const responseData = await response.json();
            return responseData;

        } catch (error) {
            // Handle error
            console.error('Error:', error.message);
        }

    }


    const handlePostRequestRcl = async (data) => {


        console.log("Inside POST request sending this data to POST:: ", JSON.stringify(data));
        const url = `http://localhost:8092/v1/api/rcl/create`;

        try {
            const response = await fetch(url, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                // body: JSON.stringify(parameterMap),
                body: JSON.stringify(data),
            });

            if (!response.ok) {
                throw new Error('Request failed');
            }

            // Handle successful response
            const responseData = await response.json();
            console.log('POST request', responseData);
            return responseData;
        } catch (error) {
            // Handle error
            console.error('Error:', error.message);
        }

    }

    const handleGetAllRiskDimension = async () => {

        const url = baseUrl + "/rd/dimension-wt";
        try {
            const response = await fetch(url);

            if (!response.ok) {
                throw new Error('Request failed');
            }
            // Handle successful response
            const responseData = await response.json();

            console.log('GET request successful', responseData);
            return responseData;

        } catch (error) {
            // Handle error
            console.error('Error:', error.message);
            return;
        }
    }

    const handleDeleteRequestRd = async (dimensionName) => {

        console.log("Inside Delete request");
        const url = `http://localhost:8092/v1/api/rd/delete/${encodeURIComponent(dimensionName)}`;

        try {
            const response = await fetch(url, {
                method: 'DELETE',
                headers: {
                    'Content-Type': 'application/json',
                },
                // body: JSON.stringify(parameterMap),
                // body: JSON.stringify(data),
            });

            if (!response.ok) {
                throw new Error('Request failed');
            }

            // Handle successful response
            console.log('DELETE request successful');
        } catch (error) {
            // Handle error
            console.error('Error:', error.message);
        }

    }
    const handlePutRequestRd = async (dimensionName, newWeight) => {

        console.log("updating ", dimensionName, newWeight);

        const data = {
            'dimension': dimensionName,
            'weight': newWeight
        };


        console.log("Inside put request sending this data to updation", JSON.stringify(data));
        const url = `http://localhost:8092/v1/api/rd/update/${encodeURIComponent(dimensionName)}`;

        try {
            const response = await fetch(url, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                },
                // body: JSON.stringify(parameterMap),
                body: JSON.stringify(data),
            });

            // if (!response.ok) {
            //     throw new Error('Request failed');
            // }

            const responseData = await response.json();
            return responseData;

        } catch (error) {
            // Handle error
            console.error('Error:', error.message);
        }

    }

    const handlePostRequestCrs = async (data) => {
        console.log(JSON.stringify(data));

        console.log("Inside POST request sending this data to POST", JSON.stringify(data));
        const url = `http://localhost:8092/v1/api/crs/create`;

        try {
            const response = await fetch(url, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                // body: JSON.stringify(parameterMap),
                body: JSON.stringify(data),
            });

            if (!response.ok) {
                throw new Error('Request failed');
            }

            // Handle successful response
            console.log('POST request successful');
        } catch (error) {
            // Handle error
            console.error('Error:', error.message);
        }
    }

    const handleGetAllRiskScoreLevel = async () => {

        const url = baseUrl + "/rsl/getall";
        try {
            const response = await fetch(url);


            if (!response.ok) {
                throw new Error('Request failed');
            }
            // Handle successful response
            const responseData = await response.json();

            console.log('GET request successful', responseData);
            return responseData;

        } catch (error) {
            // Handle error
            console.error('Error:', error.message);
            return;
        }


    }

    const handlePostRiskScoreLevel = async (request) => {

        const url = baseUrl + '/rsl/create'
        console.log("Sending post request with body:", JSON.stringify(request));

        try {


            // Send the data to the backend for validation
            const response = await fetch(url, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(request),
            });

            if (!response.ok) {
                // Handle non-200 responses from the backend
                // setErrorMessage('Error while validating the data.');

                return { severity: 'error', message: 'Error while validating the data.' };
            }

            const data = await response.json();
            console.log("response data ", data)

            if (data.interfering) {
                // Handle case when the group interferes with existing data
                // setErrorMessage('This set interferes with existing data.');
                return { severity: 'error', message: 'Data clashing with existing data.' };

            } else {
                // Handle case when the group is valid
                // Proceed with saving the data or displaying a success message
                return { severity: 'success', message: 'Saved Successfully !' };

            }
        } catch (error) {
            // Handle network errors or other issues
            // setErrorMessage('Error occurred while communicating with the server.');
            return { severity: 'error', message: 'Error occurred while communicating with the server.' };

        }
    };

    const handleDeleteRequestRsl = async (levelName) => {

        console.log("Inside Delete request");
        const url = baseUrl + `/rsl/delete/${encodeURIComponent(levelName)}`;

        try {
            const response = await fetch(url, {
                method: 'DELETE',
                headers: {
                    'Content-Type': 'application/json',
                },
            });

            const responseData = await response.json();

            if (!response.ok) {
                // If the response is not OK, throw an error with the response data
                throw new Error(responseData.error || 'Unknown error occurred');
            }

            // Return the data for successful response
            return { ...responseData, "alertSeverity": 'success' };
        } catch (error) {
            // Handle error
            console.error('Error:', error.message);
            // Return error data with alertSeverity set to 'error'
            return { error: 'Something went Wrong. Unable to delete', "alertSeverity": 'error' };
        }
    }

    const handlePutRequestRsl = async (levelName, data) => {

        console.log(levelName, JSON.stringify(data));


        console.log("Inside put request sending this data to updation", JSON.stringify(data));
        const url = baseUrl + `/rsl/update/${encodeURIComponent(levelName)}`;

        try {
            const response = await fetch(url, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                },
                // body: JSON.stringify(parameterMap),
                body: JSON.stringify(data),
            });

            if (!response.ok) {
                throw new Error('Request failed');
            }

            // Handle successful response
            const responseData = await response.json();
            console.log('PUT request successful', response, responseData);
            return responseData;
        } catch (error) {
            // Handle error
            console.error('Error:', error.message);
        }

    }

    const handleGetUniqueLevelName = async () => {

        const url = baseUrl + "/rsl/getUniqueLevelName";
        try {
            const response = await fetch(url);


            if (!response.ok) {
                throw new Error('Request failed');
            }
            // Handle successful response
            const responseData = await response.json();

            console.log('GET request successful', responseData);
            return responseData;

        } catch (error) {
            // Handle error
            console.error('Error:', error.message);
            return;
        }


    }

    const handleGetAllScoreCapData = async () => {

        const url = baseUrl + "/sc/getall";
        try {
            const response = await fetch(url);


            if (!response.ok) {
                throw new Error('Request failed');
            }
            // Handle successful response
            const responseData = await response.json();

            console.log('GET request successful', responseData);
            return responseData;

        } catch (error) {
            // Handle error
            console.error('Error:', error.message);
            return;
        }


    }

    const handleDeleteRequestSc = async (id) => {

        console.log("Inside Delete request");
        const url = baseUrl + `/sc/delete/${encodeURIComponent(id)}`;

        try {
            const response = await fetch(url, {
                method: 'DELETE',
                headers: {
                    'Content-Type': 'application/json',
                },
            });

            // const responseData = await response.json();
            // console.log(response)
            if (!response.ok) {
                // If the response is not OK, throw an error with the response data
                throw new Error('Unknown error occurred');
            }

            // Return the data for successful response
            return { message: 'Successfully Deleted', alertSeverity: 'success' };
        } catch (error) {
            // Handle error
            console.error('Error:', error.message);
            // Return error data with alertSeverity set to 'error'
            return { error: 'Something went Wrong. Unable to delete', "alertSeverity": 'error' };
        }
    }

    const handlePostRequestSc = async (request) => {

        const url = baseUrl + '/sc/create'

        const data = {
            totalRiskCappedScore: request.value,
            countCondition: request.conditionCount,
            scoreCapCondition: request.selectedCondtion,
        }
        console.log("Sending post request with body:", JSON.stringify(data));

        try {


            // Send the data to the backend for validation
            const response = await fetch(url, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(data),
            });

            if (!response.ok) {
                // Handle non-200 responses from the backend
                // setErrorMessage('Error while validating the data.');

                return { severity: 'error', message: 'Error, Not Submitted.' };
            }

            // const data = await response.json();

            // Handle case when the group is valid
            // Proceed with saving the data or displaying a success message
            return { severity: 'success', message: 'Saved Successfully !' };

        } catch (error) {
            // Handle network errors or other issues
            // setErrorMessage('Error occurred while communicating with the server.');
            return { severity: 'error', message: 'Error occurred while communicating with the server.' };

        }
    };

    const handlePutRequestSc = async (id, data) => {

        const url = baseUrl + `/sc/update/${encodeURIComponent(id)}`

        console.log(url, JSON.stringify(data));
        try {
            const response = await fetch(url, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                },
                // body: JSON.stringify(parameterMap),
                body: JSON.stringify(data),
            });

            if (!response.ok) {
                return { severity: 'error', message: 'Error, Something went wrong.' };
            }

            // Handle successful response
            return { severity: 'success', message: 'Updated Successfully !' };

        } catch (error) {
            // Handle network errors or other issues
            // setErrorMessage('Error occurred while communicating with the server.');
            return { severity: 'error', message: 'Error occurred while communicating with the server.' };

        }

    }

    const handleGetScoreCapById = async (id) => {
        const url = baseUrl + `/sc/get/${encodeURIComponent(id)}`;
        try {
            const response = await fetch(url);


            if (!response.ok) {
                // return responseData;
                throw new Error('Request failed');

            }
            const responseData = await response.json();
            // Handle successful response

            console.log('GET request successful', responseData);
            return responseData;

        } catch (error) {
            // Handle error
            console.error('Error:', error.message);
            return;
        }
    }


    return {
        handlePutRequestCrs, handlePostRequestCrs,
        handlePutRequestRcl, handlePostRequestRcl,
        handleGetAllRiskDimension, handlePutRequestRd, handleDeleteRequestRd,
        handleGetAllRiskScoreLevel, handlePostRiskScoreLevel, handleDeleteRequestRsl, handlePutRequestRsl, handleGetUniqueLevelName,
        handleGetAllScoreCapData, handleDeleteRequestSc, handlePostRequestSc, handlePutRequestSc, handleGetScoreCapById
    };
};

export default useCustomFunction;

