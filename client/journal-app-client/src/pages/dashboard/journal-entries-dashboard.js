
import {Link, useParams} from 'react-router-dom';
import useJournalEntryList from '../../facades/hooks/useJournalEntryList';
import JournalEntryCard from './journal-entries/journal-entry-card';

function JournalEntriesDashboardPage(props){
    const {journalId} = useParams();
    const [journalEntries] = useJournalEntryList(["getAll"]);


    return (
        <div id="journalEntryDashboardDiv" className="container page-div">
            <h2>Journal Entries</h2>
         
            <div className="row">
                <div className="col mb-3">
                    <Link className="btn btn-primary" to={"/"+journalId+"/new"}>+New Entry</Link>
                </div>
            </div>

            <br/><br/>


            {
                journalEntries.map(journalEntry=>
                    <>
                        <JournalEntryCard entry={journalEntry}></JournalEntryCard>
                        <br/>
                    </>
                )
            }
            
        </div>
    )
}

export default JournalEntriesDashboardPage;