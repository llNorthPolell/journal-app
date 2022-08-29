import React, { useEffect, useState } from 'react';
import {Link, useParams, useNavigate} from 'react-router-dom';
import {useData} from '../contexts/dataContext';
import {useDashboard} from '../contexts/dashboardContext';

import { DefaultJournalEntry } from './journal-entry-dto';

import JournalEntryForm from './journal-entry-form';

function JournalEntryPage(props) {
  const {createJournalEntry,updateJournalEntry, updateJournal, currentJournal} = useData();
  const {getJournalEntry} = useDashboard();

  const navigate = useNavigate();
  const {journalId, entryId} = useParams();

  const [currentEntry,setCurrentEntry] = useState(null);

  useEffect(()=>{
    async function loadData(){
      const currentEntry = await getJournalEntry(entryId);
      if (currentEntry)
        setCurrentEntry(currentEntry);
    }
    loadData();
  },[]);


  function getSchemaList(journalBodyItems){
    let schemas=[];

    journalBodyItems.forEach(journalBodyItem=>{
      let schema = {
        topic: journalBodyItem.topic,
        records: []
      };
      journalBodyItem.recordList.forEach(record=>
        schema.records.push(record.recKey)
      )
      schemas.push(schema);
    });

    return schemas;
  }

  function updateJournalHistory(journalEntry){
    let schemas=getSchemaList(journalEntry.journalBodyItems);

    console.log("Delta Schema : " + JSON.stringify(schemas)); 

    updateJournal(journalId, {...currentJournal,
      last_updated: new Date().toISOString(),
      schemas: schemas
    });
  }


  async function submitNew(formFields) {
    const output = {...formFields};
    output.journal = journalId;
    
    const returnJournalEntry = await createJournalEntry(output);
    console.log("Published " + JSON.stringify(returnJournalEntry) + " to " + journalId);
    updateJournalHistory(returnJournalEntry);
    navigate('/'+journalId);
  }


  async function submitUpdate(formFields){
    const output = {...formFields};

    await updateJournalEntry(entryId,output);
    console.log("Published " + JSON.stringify(output) + " to " + journalId);
    updateJournalHistory(output);

    navigate('/'+journalId);
  }


  return (
    <div id="newEntryFormDiv" className="container page-div">
      <div>
        <Link id="toDashboardButton" className="btn btn-outline-primary" to={"/"+journalId}>Go to Dashboard</Link>
      </div>
      <br/><br/>
      {
        (props.mode==="NEW" || !currentEntry)?
            <JournalEntryForm data={DefaultJournalEntry} journal={currentJournal} mode={props.mode} submit={submitNew}/>
        :
            <JournalEntryForm data={currentEntry} journal={currentJournal} mode={props.mode} submit={submitUpdate}/>

      }
      

    </div>
  );

}

export default JournalEntryPage;