import { Button, IconButton, Tooltip } from '@mui/material'
import React from 'react'
import { Link } from 'react-router-dom'
import './Edit.css'
import InfoIcon from '@mui/icons-material/Info';
import { TooltipInfoIcon } from '../small-components/TooltipInfoIcon';



export default function Edit() {
    return (
        <div>

            <div className='btn-container'>
                <Link to='/crsdash'>
                    <Button className='button' variant="outlined" color="primary" >
                        Manage Company Dimension Score Table
                    </Button>
                </Link>
                <TooltipInfoIcon title={'Lorem ipsum, dolor sit amet consectetur adipisicing elit. Aliquam expedita laboriosam incidunt dolorum dolorem, tempore cupiditate consequatur laudantium ipsa nemo sint distinctio totam, perspiciatis culpa, nihil beatae aperiam amet! Mollitia!'} />
            </div>
            <div className='btn-container'>
                <Link to='/rddash'>
                    <Button className='button' variant="outlined" color="primary">
                        Manage Dimension's Table
                    </Button>
                </Link>
                <TooltipInfoIcon title={'Lorem ipsum, dolor sit amet consectetur adipisicing elit. Aliquam expedita laboriosam incidunt dolorum dolorem, tempore cupiditate consequatur laudantium ipsa nemo sint distinctio totam, perspiciatis culpa, nihil beatae aperiam amet! Mollitia!'} />

            </div>
            <div className='btn-container'>
                <Link to='/rcldash'>
                    <Button className='button' variant="outlined" color="primary">
                        Manage Formula Table
                    </Button>
                </Link>
                <TooltipInfoIcon title={'Lorem ipsum, dolor sit amet consectetur adipisicing elit. Aliquam expedita laboriosam incidunt dolorum dolorem, tempore cupiditate consequatur laudantium ipsa nemo sint distinctio totam, perspiciatis culpa, nihil beatae aperiam amet! Mollitia!'} />

            </div>
            <div className='btn-container'>
                <Link to='/rsldash'>
                    <Button className='button' variant="outlined" color="primary">
                        Manage Score Levels
                    </Button>
                </Link>
                <TooltipInfoIcon title={'Lorem ipsum, dolor sit amet consectetur adipisicing elit. Aliquam expedita laboriosam incidunt dolorum dolorem, tempore cupiditate consequatur laudantium ipsa nemo sint distinctio totam, perspiciatis culpa, nihil beatae aperiam amet! Mollitia!'} />

            </div>
            <div className='btn-container'>
                <Link to='/scdash'>
                    <Button className='button' variant="outlined" color="primary">
                        Manage Score Cap
                    </Button>
                </Link>
                <TooltipInfoIcon title={'Lorem ipsum, dolor sit amet consectetur adipisicing elit. Aliquam expedita laboriosam incidunt dolorum dolorem, tempore cupiditate consequatur laudantium ipsa nemo sint distinctio totam, perspiciatis culpa, nihil beatae aperiam amet! Mollitia!'} />

            </div>
        </div>
    )
}
