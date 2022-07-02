import { BrowserRouter, Route, Routes } from 'react-router-dom';

// Scripts 
import HomePage from './home/home';
import LoginPage from './login/login';
import JournalEntryPage from './journal-entry/journal-entry';
import DashboardPage from './dashboard/dashboard';
import Navbar from './navbar/navbar';

import { AuthProvider } from './contexts/authContext';

//Demo
import DemoJournalEntries from './demo/demo-journal-entries.json'
import DemoJournalCollections from './demo/demo-journal-collections.json'
import DemoTopicList from './demo/demo-topic-list.json'
import DemoDashboard from './demo/demo-dashboard.json'

// Other Resources
import logo from './logo.svg';
import './App.css';
import { DataProvider } from './contexts/dataContext';

function App() {
  // for defaulting date field to today
  const today = new Date();
  const day = ((today.getDate() > 9) ? '' : '0') + today.getDate();
  const month = ((today.getMonth() > 9) ? '' : '0') + (today.getMonth() + 1);
  const year = today.getFullYear();

  const demoNewJournalEntry = {
    journalCollection: "",
    summary: "",
    dateOfEntry: year + "-" + month + "-" + day,
    overview: "",
    journalBodyItems: []
  }

  return (
    <div id="JournalAppDiv">
      <AuthProvider>
        <BrowserRouter>
          <Navbar></Navbar>
          <br />
          <DataProvider>
            <Routes>
              <Route path="/journal-app/" element={<HomePage />}></Route>
              <Route path="/journal-app/login" element={<LoginPage />}></Route>
              <Route path="/journal-app/:collection_id" element={<DashboardPage contents={DemoDashboard} />}></Route>
              <Route path="/journal-app/:collection_id/new" element={<JournalEntryPage data={demoNewJournalEntry} journalCollections={DemoJournalCollections} topicList={DemoTopicList} />}></Route>
              <Route path="/journal-app/:collection_id/edit/:entry_id" element={<JournalEntryPage data={DemoJournalEntries[0]} journalCollections={DemoJournalCollections} topicList={DemoTopicList} />}></Route>
            </Routes>
          </DataProvider>
        </BrowserRouter>
      </AuthProvider>
    </div>
  );
}

export default App;
