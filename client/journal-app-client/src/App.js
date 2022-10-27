import { BrowserRouter, Route, Routes, Outlet } from 'react-router-dom';

// Scripts 
import HomePage from './home/home';
import LoginPage from './login/login';
import JournalEntryPage from './journal-entry/journal-entry-page';
import DashboardPage from './dashboard/dashboard';

import Navbar from './navbar/navbar';

import { AuthProvider } from './contexts/authContext';
import { DashboardProvider } from './contexts/dashboardContext';
import PrivateRoute from './util/components/private-route';

// Other Resources
import logo from './logo.svg';
import './App.css';
import GoalPage from './goal/goal-page';
import GoalsDashboardPage from './dashboard/goals-dashboard';
import JournalEntriesDashboardPage from './dashboard/journal-entries-dashboard';


function App() {
  return (
    <div id="JournalAppDiv">
      <AuthProvider>
        <BrowserRouter>

          <div className="contentContainer">
            <Routes>
              <Route element={<><Navbar /><Outlet/></>}>
                <Route path="/" element={<HomePage />}></Route>
                <Route path="/login" element={<LoginPage />}></Route>
                <Route path="/userAgreement" element={<></>}></Route>
                <Route path="/:journalId"
                  element={
                    <PrivateRoute>
                      <DashboardProvider>
                        <DashboardPage />
                      </DashboardProvider>
                    </PrivateRoute>
                  }
                ></Route>
                <Route path="/:journalId/goals"
                  element={
                    <PrivateRoute>
                      <DashboardProvider>
                        <GoalsDashboardPage />
                      </DashboardProvider>
                    </PrivateRoute>
                  }
                ></Route>
                <Route path="/:journalId/goals/new"
                  element={
                    <PrivateRoute>
                      <GoalPage mode="NEW" />
                    </PrivateRoute>
                  }
                ></Route>
                <Route path="/:journalId/goals/:goalId"
                  element={
                    <PrivateRoute>
                      <GoalPage mode="EDIT" />
                    </PrivateRoute>
                  }
                ></Route>
                <Route path="/:journalId/entries"
                  element={
                    <PrivateRoute>
                      <JournalEntriesDashboardPage />
                    </PrivateRoute>
                  }
                ></Route>
                <Route path="/:journalId/new"
                  element={
                    <PrivateRoute>
                      <JournalEntryPage mode="NEW" />
                    </PrivateRoute>
                  }
                ></Route>
                <Route path="/:journalId/:entryId"
                  element={
                    <PrivateRoute>
                      <JournalEntryPage mode="EDIT" />
                    </PrivateRoute>
                  }
                ></Route>
              </Route>
            </Routes>
          </div>
        </BrowserRouter>
      </AuthProvider>
    </div>
  );
}

export default App;
