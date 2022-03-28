import React, {useState} from 'react';
import JournalBodyItemForm from './journal-body-item-form';
import JournalBodyItem from './journal-body-item';
import {v4 as uuidv4} from 'uuid';
import SimpleInput from '../util/components/simple-input';
import useSimpleState from '../util/hooks/useSimpleState';

function NewEntryPage(){
  // for defaulting date field to today
  const today = new Date();
  const day = today.getDate();
  const month = ((today.getMonth()>9)?'':'0') + (today.getMonth() +1);
  const year = today.getFullYear();

  const [topicList,setTopicList]= useState([]);
  const [journalBodyItems,setJournalBodyItems] = useState([]);
  const [usedTopics,setUsedTopics]= useState([]);

  // Simple States
  const [journalCollection,handleChangeJournalCollection] = useSimpleState("Select Collection");
  const [summary,handleChangeSummary] = useSimpleState("Hello World!");
  const [dateOfEntry,handleChangeDateOfEntry] = useSimpleState(year+"-"+month+"-"+day);
  const [overview,handleChangeOverview] = useSimpleState("");

  const addToBody = (topic,comment,records) => {
    setJournalBodyItems([...journalBodyItems, {id:uuidv4(), topic: topic, comment: comment, records: records }]);
  }

  const removeFromBody = (id) => {
    const updatedJournalBodyItemsList = journalBodyItems.filter(journalBodyItem => journalBodyItem.id!==id);
    setJournalBodyItems(updatedJournalBodyItemsList);
  }

  const saveTopic = (newTopic) =>{ 
    setTopicList([...topicList,newTopic]);
  }

  const addToUsedTopics = (usedTopic) => {
    setUsedTopics([...usedTopics,usedTopic]);
  }

  const removeFromUsedTopics = (topic) => {
    const updatedUsedTopics = usedTopics.filter(usedTopic => usedTopic!==topic);
    setUsedTopics(updatedUsedTopics);
  }

  return (
    <div id="newEntryFormDiv" className="container">
      <form>
        <SimpleInput 
          id="collectionDropdown" 
          value={journalCollection} 
          fieldName="collection" 
          type="select" 
          optionList={["Select Collection", "Test Collection"]} 
          handleUpdate={handleChangeJournalCollection}></SimpleInput>  
        <SimpleInput 
          id="summaryField" 
          value={summary} 
          fieldName="summary"
          displayName="Summary" 
          type="text" 
          handleUpdate={handleChangeSummary}></SimpleInput>
        <SimpleInput 
          id="dateOfEntryField" 
          value={dateOfEntry} 
          fieldName="dateOfEntry"
          displayName="Date" 
          type="date" 
          handleUpdate={handleChangeDateOfEntry}></SimpleInput>    
        <SimpleInput 
          id="overviewField" 
          value={overview} 
          fieldName="overview"  
          displayName="Overview" 
          type="textarea" 
          handleUpdate={handleChangeOverview}></SimpleInput>

        <label htmlFor="journalBodyDiv" className="form-label">Body</label>
        <div id="journalBodyDiv"> 
          <JournalBodyItemForm 
            addToBody={addToBody} 
            saveTopic={saveTopic} 
            topicList={topicList} 
            usedTopics={usedTopics} 
            addToUsedTopics={addToUsedTopics}>
          </JournalBodyItemForm>
          <br/>
            {
              journalBodyItems.map(journalBodyItem => (
                <JournalBodyItem 
                  id={journalBodyItem.id} 
                  key={journalBodyItem.id} 
                  topic={journalBodyItem.topic} 
                  comment={journalBodyItem.comment} 
                  records={journalBodyItem.records}
                  removeFromBody={removeFromBody}
                  removeFromUsedTopics={removeFromUsedTopics}
                  >
                </JournalBodyItem>
              ))
            }
          
        </div>
      </form>
    </div>
  );

}

export default NewEntryPage;