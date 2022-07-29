import { BrowserRouter, Route, Routes } from 'react-router-dom';

// Scripts 
import HomePage from './home/home';
import LoginPage from './login/login';
import JournalEntryPage from './journal-entry/journal-entry';
import DashboardPage from './dashboard/dashboard';
import Navbar from './navbar/navbar';

import { AuthProvider } from './contexts/authContext';
import { DataProvider } from './contexts/dataContext';
import { DashboardProvider } from './contexts/dashboardContext';
import PrivateRoute from './util/components/private-route';

import { DefaultJournalEntry } from './journal-entry/journal-entry-dto';

//Demo
import DemoJournalEntries from './demo/demo-journal-entries.json'

// Other Resources
import logo from './logo.svg';
import './App.css';


function App() {
  return (
    <div id="JournalAppDiv">
      <AuthProvider>
        <BrowserRouter>
          <Navbar></Navbar>
          <div className="contentContainer">
            <DataProvider>
              <DashboardProvider>
                <Routes>
                  <Route path="/" element={<HomePage />}></Route>
                  <Route path="/login" element={<LoginPage />}></Route>
                  <Route path="/userAgreement" element={<></>}></Route>
                  <Route path="/:journalId" element={<PrivateRoute><DashboardPage /></PrivateRoute>}></Route>
                  <Route path="/:journalId/new" element={<PrivateRoute><JournalEntryPage data={DefaultJournalEntry}/></PrivateRoute>}></Route>
                  <Route path="/:journalId/:entry_id" element={<PrivateRoute><JournalEntryPage data={DemoJournalEntries[0]} /></PrivateRoute>}></Route>
                </Routes>
              </DashboardProvider>
            </DataProvider>
          </div>
        </BrowserRouter>
      </AuthProvider>
    </div>
  );
}

export default App;
