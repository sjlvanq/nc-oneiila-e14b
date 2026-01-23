# Copyright (c) Microsoft. All rights reserved.

import asyncio
from random import randint
from typing import Annotated

from agent_framework import AgentThread, ChatAgent
from agent_framework.azure import AzureOpenAIAssistantsClient
from azure.identity import AzureCliCredential
from pydantic import Field

"""
Azure OpenAI Assistants with Thread Management Example

This sample demonstrates thread management with Azure OpenAI Assistants, comparing
automatic thread creation with explicit thread management for persistent context.
"""


def get_weather(
    location: Annotated[str, Field(description="The location to get the weather for.")],
) -> str:
    """Get the weather for a given location."""
    conditions = ["sunny", "cloudy", "rainy", "stormy"]
    return f"The weather in {location} is {conditions[randint(0, 3)]} with a high of {randint(10, 30)}°C."


async def example_with_automatic_thread_creation() -> None:
    """Example showing automatic thread creation (service-managed thread)."""
    print("=== Automatic Thread Creation Example ===")
    print("Each call to agent.run() creates a new thread (no context memory).")
    print("Type 'quit' to exit this example.\n")

    # For authentication, run `az login` command in terminal or replace AzureCliCredential with preferred
    # authentication option.
    async with ChatAgent(
        chat_client=AzureOpenAIAssistantsClient(credential=AzureCliCredential()),
        instructions="You are a helpful weather agent.",
        tools=get_weather,
    ) as agent:
        conversation_count = 0
        
        while True:
            conversation_count += 1
            print(f"Conversation #{conversation_count} (new thread):")
            
            user_input = input("User: ").strip()
            
            if user_input.lower() in ['quit', 'exit', 'salir']:
                print("Agent: Goodbye!")
                break
                
            if not user_input:
                continue
            
            # Each run() call creates a new thread automatically
            result = await agent.run(user_input)
            print(f"Agent: {result.text}")
            
            if conversation_count == 1:
                print("\n(Note: Try asking about a previous city in the next message to see the agent has no memory)")
            
            print()  # Add spacing between conversations


async def example_with_thread_persistence() -> None:
    """Example showing thread persistence across multiple conversations."""
    print("=== Thread Persistence Example ===")
    print("Using the same thread across multiple conversations to maintain context.")
    print("Type 'quit' to exit this example.\n")

    # For authentication, run `az login` command in terminal or replace AzureCliCredential with preferred
    # authentication option.
    async with ChatAgent(
        chat_client=AzureOpenAIAssistantsClient(credential=AzureCliCredential()),
        instructions="You are a helpful weather agent.",
        tools=get_weather,
    ) as agent:
        # Create a new thread that will be reused
        thread = agent.get_new_thread()
        message_count = 0
        
        print("Agent: Hello! I'm your weather assistant. I'll remember our conversation!")
        
        while True:
            message_count += 1
            user_input = input(f"\nUser (message #{message_count}): ").strip()
            
            if user_input.lower() in ['quit', 'exit', 'salir']:
                print("Agent: Goodbye! It was nice talking to you!")
                break
                
            if not user_input:
                continue
            
            # Use the same thread to maintain context
            result = await agent.run(user_input, thread=thread)
            print(f"Agent: {result.text}")
            
            if message_count == 1:
                print("(Note: I'll remember our previous questions in this conversation)")
        
        print("\nNote: The agent remembers context from previous messages in the same thread.\n")


async def example_with_existing_thread_id() -> None:
    """Example showing how to work with an existing thread ID from the service."""
    print("=== Existing Thread ID Example ===")
    print("Using a specific thread ID to continue an existing conversation.")
    print("Type 'quit' to exit this example.\n")

    # First, create a conversation and capture the thread ID
    existing_thread_id = None

    # For authentication, run `az login` command in terminal or replace AzureCliCredential with preferred
    # authentication option.
    async with ChatAgent(
        chat_client=AzureOpenAIAssistantsClient(credential=AzureCliCredential()),
        instructions="You are a helpful weather agent.",
        tools=get_weather,
    ) as agent:
        # Start a conversation and get the thread ID
        thread = agent.get_new_thread()
        
        print("Agent: Hello! I'm your weather assistant. Let's start our conversation.")
        
        while True:
            user_input = input("\nUser: ").strip()
            
            if user_input.lower() in ['quit', 'exit', 'salir']:
                print("Agent: Goodbye!")
                break
                
            if not user_input:
                continue
            
            result = await agent.run(user_input, thread=thread)
            print(f"Agent: {result.text}")
            
            # The thread ID is set after the first response
            if not existing_thread_id and thread.service_thread_id:
                existing_thread_id = thread.service_thread_id
                print(f"\nThread ID captured: {existing_thread_id}")
                print("You can now continue this conversation in a new session using this ID.")
                break

    if existing_thread_id:
        print("\n--- Continuing with the same thread ID in a new agent instance ---")

        # Create a new agent instance but use the existing thread ID
        async with ChatAgent(
            chat_client=AzureOpenAIAssistantsClient(thread_id=existing_thread_id, credential=AzureCliCredential()),
            instructions="You are a helpful weather agent.",
            tools=get_weather,
        ) as agent:
            # Create a thread with the existing ID
            thread = AgentThread(service_thread_id=existing_thread_id)
            
            print("Agent: Welcome back! I remember our previous conversation.")
            
            while True:
                user_input = input("\nUser: ").strip()
                
                if user_input.lower() in ['quit', 'exit', 'salir']:
                    print("Agent: Goodbye!")
                    break
                    
                if not user_input:
                    continue
                
                result = await agent.run(user_input, thread=thread)
                print(f"Agent: {result.text}")
            
            print("Note: The agent continues the conversation from the previous thread.\n")


async def main() -> None:
    print("=== Azure OpenAI Assistants Chat Client Agent Thread Management Examples ===\n")

    while True:
        print("Choose an example:")
        print("1. Automatic Thread Creation (no memory)")
        print("2. Thread Persistence (with memory)")
        print("3. Existing Thread ID (continue conversation)")
        print("4. Exit")
        
        choice = input("\nEnter choice (1-4): ").strip()
        
        if choice == "1":
            await example_with_automatic_thread_creation()
        elif choice == "2":
            await example_with_thread_persistence()
        elif choice == "3":
            await example_with_existing_thread_id()
        elif choice == "4":
            print("Goodbye!")
            break
        else:
            print("Invalid choice. Please try again.\n")
        
        print("\n" + "="*60 + "\n")


if __name__ == "__main__":
    asyncio.run(main())