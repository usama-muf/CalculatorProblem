import React from 'react'
import './Header.css'
import { useNavigate } from 'react-router-dom'; // Import useNavigate from 'react-router-dom'

export default function Header() {

    const navigate = useNavigate(); // Initialize the useNavigate hook

    const handleGoBack = () => {
        navigate(-1); // Navigate back one step in the history
    };

    const handleGoHome = () => {
        navigate('/');
    };

    return (
        <header className="header">
            <button onClick={handleGoBack} className="button">
                Go Back
            </button>
            <h1 className="title">Company Calculator</h1>
            <button onClick={handleGoHome} className="button">
                Go to Home
            </button>
        </header>
    )
}
