import {
    ChakraProvider,
    Box,
    VStack,
    Alert,
    AlertIcon,
    AlertTitle,
    AlertDescription,
    CloseButton,
    Button
} from "@chakra-ui/react";

import React from "react";

export default function AlertSuccess(){
    const [display, setDisplay] = React.useState('none');

    return(
        <ChakraProvider>
            <Box p={4}>
                <VStack>
                    <Button onClick={()=> setDisplay('')}>Click to look</Button>
                    <Alert status="success" variant="top-accent">
                        <AlertIcon />
                        <AlertTitle mr={3}>Stop</AlertTitle>
                        <AlertDescription>Success!</AlertDescription>
                        <CloseButton position="absolute" right="8px" top="8px" />
                    </Alert>
                </VStack>
            </Box>
        </ChakraProvider>
    );
}