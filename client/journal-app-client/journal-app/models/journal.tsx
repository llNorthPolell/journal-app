import {z} from 'zod'


const JournalSchema = z.object(
    {
        id: z.string(),
        name: z.string(),
        author: z.string(),
        img: z.string()
    }
)