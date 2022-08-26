import { BrowserRouter, Route, Routes } from 'react-router-dom';

// Scripts 
import HomePage from './home/home';
import LoginPage from './login/login';
import JournalEntryPage from './journal-entry/journal-entry';
import DashboardPage from './dashboard/dashboard';

import Navbar from './navbar/navbar';
import Toolbar from './toolbar/toolbar';

import { AuthProvider } from './contexts/authContext';
import { DataProvider } from './contexts/dataContext';
import { DashboardProvider } from './contexts/dashboardContext';
import PrivateRoute from './util/components/private-route';

// Other Resources
import logo from './logo.svg';
import './App.css';


function App() {
  return (
    <div id="JournalAppDiv">
      <AuthProvider>
        <BrowserRouter>
          <Navbar/>
          <div className="contentContainer">
            <DataProvider>
                <Routes>
                  <Route path="/" element={<HomePage />}></Route>
                  <Route path="/login" element={<LoginPage />}></Route>
                  <Route path="/userAgreement" element={<></>}></Route>
                  <Route path="/:journalId" 
                    element={
                      <PrivateRoute>
                        <DashboardProvider>
                          <DashboardPage />
                          <Toolbar/>
                        </DashboardProvider>
                      </PrivateRoute>
                    }
                  ></Route>
                  <Route path="/:journalId/goals" 
                    element={
                      <PrivateRoute>
                        <DashboardProvider>
                          <h1>Goals Coming Soon!</h1>
                          <Toolbar/>
                        </DashboardProvider>
                      </PrivateRoute>
                    }
                  ></Route>
                  <Route path="/:journalId/entries" 
                    element={
                      <PrivateRoute>
                        <DashboardProvider>
                          <h1>Entries Coming Soon!</h1>
                          <Toolbar/>
                        </DashboardProvider>
                      </PrivateRoute>
                    }
                  ></Route>
                  <Route path="/:journalId/new" 
                    element={
                      <PrivateRoute>
                        <DashboardProvider>
                          <JournalEntryPage mode="NEW"/>
                        </DashboardProvider>
                      </PrivateRoute>
                    }
                  ></Route>
                  <Route path="/:journalId/:entryId" 
                    element={
                      <PrivateRoute>
                        <DashboardProvider>
                          <JournalEntryPage mode="EDIT"/>
                        </DashboardProvider>
                      </PrivateRoute>
                    }
                  ></Route>
                </Routes>
            </DataProvider>
          </div>
        </BrowserRouter>
      </AuthProvider>
    </div>
  );
}

export default App;
