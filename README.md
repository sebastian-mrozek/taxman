IN PROGRESS, not usable yet.

A simple web app for contractors with few customers and little complexities in expenditure.
Based on a software engineering contractor life in NZ - hence NZ tax specific.

A reason to build a Svelte app and avoid paying an accountant.

TODO now:
- simplify expense persistence and model (tax value)
- configurable persistence folder location 
- generate test data
- tests for persistence - roundtrip test would cover ser/deser and mapping
- lock files when writing - cleanup persistence impl
- add date paid on expenses?
- add date paid on donations?

TODO later:
- svelte routing
- openAPI to generate both models? maybe Java => OpenAPI => Typescript?
- persistence (h2, json?)
- maybe remove Java, use Node and server-less?