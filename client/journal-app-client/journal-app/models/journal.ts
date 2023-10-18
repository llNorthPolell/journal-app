import {z} from 'zod'

const MAX_FILE_SIZE=5000000;
const ACCEPTED_IMAGE_TYPES = ["image/jpeg", "image/jpg", "image/png", "image/bmp"];

export const JournalSchema = z.object(
    {
        name: z.string().trim().nonempty().max(32),
        img: z.any()
            .refine((files) => files?.length ===0 || files?.[0]?.size <= MAX_FILE_SIZE, `Max file size is 5MB.`)
            .refine(
            (files) => files?.length ===0 || ACCEPTED_IMAGE_TYPES.includes(files?.[0]?.type),
            ".jpg, .jpeg, .png and .webp files are accepted."
            )
    }
            
)

export type JournalForm = {
    name: string,
    img?: File[]
}


export type JournalAPIInputFormat = {
    name: string,
    img: string
}

export interface Journal {
    name: string,
    creationTimestamp: Date,
    lastUpdated: Date,
    author: string,
    img: string,
    id: string
}
