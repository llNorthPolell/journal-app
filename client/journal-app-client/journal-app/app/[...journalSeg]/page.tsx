import Dashboard from '../../components/journal-main/dashboard';
import { PageProps } from '../../.next/types/app/page';
import { getIDTokenOrRedirectToLogin } from '../../lib/auth';
import JournalEntriesScreen from '../../components/journal-main/journalEntries';
import JournalEntryForm from '../../components/journal-main/journalEntries/journalEntryForm';
import { JournalEntry } from '../../models/journalEntry';
import JournalEntryView from '../../components/journal-main/journalEntries/journalEntryView';
import { getJournalById } from '../../lib/journalAPI';
import { getJournalEntriesInJournal } from '../../lib/journalEntriesAPI';

export default async function JournalMain({ params }: PageProps) {
    
    const journalSeg = params.journalSeg;
    const journalId = journalSeg[0];
    const id_token = await getIDTokenOrRedirectToLogin(`/${journalId}`);

    const getJournalResult = await getJournalById(journalId,id_token);
    const journal = getJournalResult.contents;

    const getJournalEntriesResult = await getJournalEntriesInJournal(journalId,id_token);
    let journalEntries = getJournalEntriesResult.contents;

    let screen = null;

    if(journalEntries)
        switch (journalSeg[1]){
            case "entries":
                if (!journalSeg[2])
                    screen = <JournalEntriesScreen journalId={journalId} journalEntries={journalEntries}/>
                else if (journalSeg[2]=="new")
                    screen = <JournalEntryForm edit={true} journalId={journalId} journalEntries={journalEntries} />
                else {
                    const journalEntry = journalEntries.find((journalEntry: JournalEntry) => journalEntry.entryId == journalSeg[2])
                    screen = <JournalEntryView journalEntry={journalEntry} journalId={journalId} />
                    //screen = <JournalEntryForm edit={true} journalEntry={journalEntry} journalId={journalId} />
                }
                
                break;   
            default:
                screen = <Dashboard journal={journal} />
                break;
        }

    return (
        <div className="journal-main">
            <h1 className="journal-main__header"> 
            {
                (journal)?
                journal.name
                :
                "This journal does not belong to you..."
            }
            </h1>
            {screen}
        </div>
    )

}
