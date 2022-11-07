import JournalEntryForm from "../../pages/journal-entry/journal-entry-form";
import {DefaultJournalEntry} from '../../pages/journal-entry/journal-entry-dto';

export default{
    title: "Forms/Journal Entry",
    component: JournalEntryForm
}



export const JournalEntryFormStory = () => 
    <JournalEntryForm data={DefaultJournalEntry}  mode="NEW"/>