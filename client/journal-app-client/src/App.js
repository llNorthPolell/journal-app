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
import DemoTopicList from './demo/demo-topic-list.json'
import DemoDashboard from './demo/demo-dashboard.json'

// Other Resources
import logo from './logo.svg';
import './App.css';


function App() {
  return (
    <div id="JournalAppDiv">
      <AuthProvider>
        <BrowserRouter>
          <Navbar></Navbar>
          <br />
          <DataProvider>
            <DashboardProvider>
              <Routes>
                <Route path="/journal-app/" element={<HomePage />}></Route>
                <Route path="/journal-app/login" element={<LoginPage />}></Route>
                <Route path="/journal-app/userAgreement" element={<></>}></Route>
                <Route path="/journal-app/:journalId" element={<PrivateRoute><DashboardPage contents={DemoDashboard} /></PrivateRoute>}></Route>
                <Route path="/journal-app/:journalId/new" element={<PrivateRoute><JournalEntryPage data={DefaultJournalEntry}/></PrivateRoute>}></Route>
                <Route path="/journal-app/:journalId/edit/:entry_id" element={<PrivateRoute><JournalEntryPage data={DemoJournalEntries[0]} topicList={DemoTopicList} /></PrivateRoute>}></Route>
              </Routes>
            </DashboardProvider>
          </DataProvider>
        </BrowserRouter>
      </AuthProvider>
    </div>
  );
}

export default App;
