import logo from './logo.svg';
import './App.css';
import JournalEntryPage from './journal-entry/journal-entry';
import DashboardPage from './dashboard/dashboard';

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

  const demoJournalEntry = {
    journalCollection: "Guitar Practice",
    summary: "At vero eos et accusamus et iusto odio",
    dateOfEntry: "2022-03-25",
    overview: "At vero eos et accusamus et iusto odio"
    +"dignissimos ducimus, qui blanditiis praesentium voluptatum deleniti"
    +"atque corrupti, quosdolores et quas molestias excepturi sint, obcaecati"
    +"cupiditate non provident, similique sunt in culpa, qui officia deserunt"
    +"mollitiaanimi, id est laborum et dolorum fuga. Et harum quidem rerum"
    +"facilis est et expedita distinctio. Nam libero tempore, cum solutanobis"
    +"est eligendi optio, cumque nihil impedit, quo minus id, quod maxime"
    +"placeat, facere possimus, omnis voluptas assumendaest, omnis dolor"
    +"repellendus.",
    journalBodyItems: [
      {
        topic: "C Major Scale", 
        description: "Et harum quidem rerum"
        +"facilis est et expedita distinctio. Nam libero tempore, cum solutanobis"
        +"est eligendi optio", 
        recordList: [
          {
            id: 1231231, 
            key: "BPM", 
            value: 120
          },
          {
            id: 1231232, 
            key: "Accuracy (%)", 
            value: 70
          }
        ]
      }
    ]
  }

  const demoJournalCollections = [
    "Guitar Practice", "Investments","Project 1"
  ]

  const topicList = [
    "C Major Scale", "Dragonforce - Through the Fire and Flames", "C Minor Arpeggios"
  ]

  return (
    //<div>
      //<JournalEntryPage data={demoJournalEntry} journalCollections={demoJournalCollections} topicList={topicList}></JournalEntryPage>
    //</div>
    <div>
      <DashboardPage></DashboardPage>
    </div>
  );
}

export default App;
