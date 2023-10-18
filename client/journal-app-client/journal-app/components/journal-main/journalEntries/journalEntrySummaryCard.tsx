import Link from "next/link"
import { JournalEntry } from "../../../models/journalEntry"

interface JournalEntrySummaryCardProps {
    journalId: string,
    journalEntry: JournalEntry
}

export default function JournalEntrySummaryCard (props:JournalEntrySummaryCardProps){
    const journalEntryId = props.journalEntry.entryId;

    return (
        <Link key={journalEntryId} href={`/${props.journalId}/entries/${journalEntryId}`} className="journal-entry-summary animated-entry-left">
            <div>
                <div className= "journal-entry-summary__heading">
                    <h3 className="heading-tertiary">{props.journalEntry.summary}</h3>
                    <p className="journal-entry-summary__created paragraph">{props.journalEntry.creationTimestamp.toString()}</p>
                </div>
                <div className="journal-entry-summary__body">
                    <p className="paragraph">{props.journalEntry.overview}</p>
                </div>
            </div>
        </Link>
    )
}