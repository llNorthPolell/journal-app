import {Link, useParams, useNavigate} from 'react-router-dom';

import { DefaultJournalEntry } from './journal-entry-dto';

import JournalEntryForm from './journal-entry-form';
import useJournalList from '../facades/hooks/useJournalList';
import useJournalEntryList from '../facades/hooks/useJournalEntryList';
import useSession from '../facades/hooks/useSession';


function JournalEntryPage(props) {
  const navigate = useNavigate();
  const {journalId,entryId} = useParams();

  const [currentJournal,currentJournalEntry]=useSession(["currentJournal", "currentJournalEntry"]);

  const [updateJournal] = useJournalList(["update"]);
  const [createJournalEntry,updateJournalEntry] = useJournalEntryList(["insert","update"]);


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
    let usedSchemas=getSchemaList(journalEntry.journalBodyItems);
    let schemasToSave = [...currentJournal.schemas];

    console.log("Delta Schema : " + JSON.stringify(usedSchemas)); 

    usedSchemas.forEach(usedSchema=>{
        if (!schemasToSave.some(saveSchema => saveSchema.topic === usedSchema.topic))
          schemasToSave.push(usedSchema);
        else {
          schemasToSave = schemasToSave.map(schema=>{
              if(schema.topic === usedSchema.topic){
                const newRecords=usedSchema.records.filter(record=> !schema.records.includes(record));
                return ({
                    topic: schema.topic,
                    records: [schema.records,...newRecords]
                });
              }
              else
                return schema;
          });
        }
    });

    updateJournal(journalId, {
      last_updated: new Date().toISOString(),
      schemas: schemasToSave
    });
  }


  async function submitNew(formFields) {
    const output = {...formFields, journal: journalId};
    
    const returnJournalEntry = await createJournalEntry(output);
    console.log("Published " + JSON.stringify(returnJournalEntry) + " to " + journalId);
    updateJournalHistory(returnJournalEntry);
    navigate('/'+journalId);
  }


  async function submitUpdate(formFields){
    const output = {...formFields};

    console.log ("Updating journal entry with " + JSON.stringify(output));
    const returnJournalEntry = await updateJournalEntry(entryId,output);
    console.log("Published " + JSON.stringify(returnJournalEntry) + " to " + journalId);
    updateJournalHistory(returnJournalEntry);

    navigate('/'+journalId);
  }


  return (
    <div id="newEntryFormDiv" className="container page-div">
      <div>
        <Link id="toDashboardButton" className="btn btn-outline-primary" to={"/"+journalId}>Go to Dashboard</Link>
      </div>
      <br/><br/>
      {
        (props.mode==="NEW" || !currentJournalEntry)?
            <JournalEntryForm data={DefaultJournalEntry} journal={currentJournal} mode={props.mode} submit={submitNew}/>
        :
            <JournalEntryForm data={currentJournalEntry} journal={currentJournal} mode={props.mode} submit={submitUpdate}/>

      }
      

    </div>
  );

}

export default JournalEntryPage;