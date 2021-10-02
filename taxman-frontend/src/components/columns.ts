export type ColumnTypeFieldValue = {
  field: string;
};

export type ColumnTypeFieldFunction = {
  getText: (item) => string;
};

export type ColumnTypeComponent = {
  component: any;
  getProps: (item) => any;
};

export type ColumnType = ColumnTypeFieldValue | ColumnTypeFieldFunction | ColumnTypeComponent;

export type Column = {
  label: string;
  type: ColumnType;
  alignRight?: boolean;
};

export function isFieldValue(o: ColumnType): o is ColumnTypeFieldValue {
  return (o as ColumnTypeFieldValue).field !== undefined;
}

export function isFieldFunction(o: ColumnType): o is ColumnTypeFieldFunction {
  return (o as ColumnTypeFieldFunction).getText !== undefined;
}

export function isComponent(o: ColumnType): o is ColumnTypeComponent {
  return (o as ColumnTypeComponent).component !== undefined && (o as ColumnTypeComponent).getProps !== undefined;
}
