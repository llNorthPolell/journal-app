import { FieldError } from "react-hook-form"

interface FormErrorProps{
    field?: any,
    message?: string
}

export default function FormError (props:FormErrorProps){
    return (
        (props.field) ?
        <p className="form__error"> {props.message} </p>
        :
        <></>
    )
}