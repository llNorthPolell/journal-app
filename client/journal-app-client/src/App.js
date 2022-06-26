import {BrowserRouter, Route, Routes} from 'react-router-dom';

import logo from './logo.svg';
import './App.css';
import JournalEntryPage from './journal-entry/journal-entry';
import DashboardPage from './dashboard/dashboard';

//Demo
import DemoJournalEntries from './demo/demo-journal-entries.json'
import DemoJournalCollections from './demo/demo-journal-collections.json'
import DemoTopicList from './demo/demo-topic-list.json'
import DemoDashboard from './demo/demo-dashboard.json'


function App() {
  // for defaulting date field to today
  const today = new Date();
  const day = ((today.getDate() > 9) ? '' : '0') + today.getDate();
  const month = ((today.getMonth() > 9) ? '' : '0') + (today.getMonth() + 1);
  const year = today.getFullYear();

  const demoNewJournalEntry = {
    journalCollection: "",
    summary: "",
    dateOfEntry: year+"-"+month+"-"+day,
    overview: "",
    journalBodyItems: []
  }

  return (
    <div id="JournalAppDiv">
      <BrowserRouter>
        <Routes>
          <Route path="/journal-app/" element={<DashboardPage contents={DemoDashboard}/>}></Route>
          <Route path="/journal-app/entry/new" element={<JournalEntryPage data={demoNewJournalEntry} journalCollections={DemoJournalCollections} topicList={DemoTopicList}/>}></Route>
          <Route path="/journal-app/entry/read/:id" element={<JournalEntryPage data={DemoJournalEntries[0]} journalCollections={DemoJournalCollections} topicList={DemoTopicList}/>}></Route>
        </Routes>
      </BrowserRouter>
    </div>
  );
}

export default App;
