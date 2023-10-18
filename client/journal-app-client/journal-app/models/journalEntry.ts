import {z} from 'zod'

export const JournalRecordSchema = z.object(
    {
        recKey: z.string().trim().nonempty().regex(new RegExp(/^[a-zA-Z_][a-zA-Z0-9_]+$/)),
        recValue: z.string().trim().nonempty()
    }
)

export const JournalBodyItemSchema = z.object(
    {
        topic: z.string().trim().nonempty(),
        description: z.string(),
        recordList: z.array(JournalRecordSchema)
    }   
)

export const JournalEntrySchema = z.object(
    {
        summary: z.string().trim().nonempty(),
        overview: z.string(),
        dateOfEntry: z.string()
                    .nonempty()
                    .transform((str)=>new Date(str)),
        journalBodyItems: z.array(JournalBodyItemSchema)
    }
            
)



export type JournalEntryForm = {
    summary: string,
    overview: string,
    dateOfEntry: string,
    journalBodyItems: JournalBodyItem[]
}

export const defaultJournalEntryForm : JournalEntryForm = {
    summary:"",
    overview: "",
    dateOfEntry: new Date().toISOString().split('.')[0].slice(0,16),
    journalBodyItems: []
}


export interface JournalEntry {
    entryId: string,
    summary: string,
    overview: string,
    dateOfEntry: string,
    creationTimestamp: Date,
    lastUpdated: Date,
    journalBodyItems: JournalBodyItem[]
}

export type JournalBodyItem = {
    topic: string,
    description: string,
    recordList: JournalRecord[]
}

export const defaultJournalBodyItem : JournalBodyItem = {
    topic:"",
    description: "",
    recordList: []
}


export type JournalRecord = {
    recKey: string,
    recValue: string
}

export const defaultJournalRecord : JournalRecord = {
    recKey:"",
    recValue: ""
}