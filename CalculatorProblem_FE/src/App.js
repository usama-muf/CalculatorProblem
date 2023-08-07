import { BrowserRouter, Route, Routes } from 'react-router-dom';
import './App.css';
import Footer from './containers/header&footer/Footer';
import Header from './containers/header&footer/Header';
import OutputTable from './containers/home/OutputTable';
import Add from './containers/temp/Add';
import Edit from './containers/temp/Edit';
import RclDashboard from './containers/temp/RclDashboard';
import CrsDashboard from './containers/temp/CrsDashboard';
import EditCrsDashboard from './containers/temp/EditCrsDashboard'
import RdDashboard from './containers/temp/RdDashboard';
import NoPage from './containers/header&footer/NoPage'
import NewCrsDashBoard from './containers/temp/NewCrsDashBoard';
import NewRdDashboard from './containers/temp/NewRdDashboard';
import NewRclDashboard from './containers/temp/NewRclDashboard';
import ScoreLevel from './containers/temp/ScoreLevel';
import NewRslDashboard from './containers/temp/NewRslDashboard';
import EditRslDashboard from './containers/temp/EditRslDashboard';
import ScDashboard from './containers/temp/ScDashboard';
import NewScDashboard from './containers/temp/NewScDashboard';
import EditScDashboard from './containers/temp/EditScDashboard';
function App() {
  return (

    <>
      <BrowserRouter>
        <Header />
        <Routes>

          <Route path='/' element={<OutputTable />} />

          <Route path='/add' element={<Add />} />
          <Route path='/edit' element={<Edit />} />

          <Route path='/crsdash' element={<CrsDashboard />} />
          <Route path='/newcrsdash' element={<NewCrsDashBoard />} />
          <Route path='/crsdash/edit/:companyName' element={<EditCrsDashboard />} />

          <Route path='/rddash' element={<RdDashboard />} />
          <Route path='/newrddash' element={<NewRdDashboard />} />

          <Route path='/rcldash' element={<RclDashboard />} />
          <Route path='/newrcldash' element={<NewRclDashboard />} />


          <Route path='/rsldash' element={<ScoreLevel />} />
          <Route path='/newrsldash' element={<NewRslDashboard />} />
          <Route path='/rsldash/edit/:levelName' element={<EditRslDashboard />} />

          <Route path='/scdash' element={<ScDashboard />} />
          <Route path='/newscdash' element={<NewScDashboard />} />
          <Route path='/scdash/edit/:id' element={<EditScDashboard />} />

          <Route path='*' element={<NoPage />} />

        </Routes>
        <Footer />
      </BrowserRouter>

    </>


    // <div className="App">
    // </div>
  );
}

export default App;
