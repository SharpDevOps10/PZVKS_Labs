-- ПЗВПКС
-- Лабораторна робота ЛР5
-- Повідомлення. Мова Ada. Механізм рандеву
-- Варіант 14
-- A = max(C)*Z + D*(MX*MS)
-- Тимофеєв Даниіл Костянтинович
-- Група ІМ-22
-- Дата 24.05.2025

with Ada.Text_IO;
use Ada.Text_IO;
with Ada.Integer_Text_IO;
use Ada.Integer_Text_IO;

procedure Lab5 is
  N: integer := 2000;
  P: integer := 8;
  H: integer := N/P;

  -- Формування типів
  type Vector_General is array(integer range <>) of integer;
  subtype Vector is Vector_General(1..N);
  subtype Vector_7H is Vector_General(1..7*H);
  subtype Vector_6H is Vector_General(1..6*H);
  subtype Vector_5H is Vector_General(1..5*H);
  subtype Vector_4H is Vector_General(1..4*H);
  subtype Vector_3H is Vector_General(1..3*H);
  subtype Vector_2H is Vector_General(1..2*H);
  subtype Vector_H is Vector_General(1..H);

  type Matrix_General is array(integer range <>) of Vector;
  subtype Matrix is Matrix_General(1..N);
  subtype Matrix_7H is Matrix_General(1..7*H);
  subtype Matrix_6H is Matrix_General(1..6*H);
  subtype Matrix_5H is Matrix_General(1..5*H);
  subtype Matrix_4H is Matrix_General(1..4*H);
  subtype Matrix_3H is Matrix_General(1..3*H);
  subtype Matrix_2H is Matrix_General(1..2*H);
  subtype Matrix_H is Matrix_General(1..H);

  -- Опис специфікації задач
  task T1 is
     pragma Storage_Size(2_147_483_648);
     entry Send_Data_From_T2_To_T1(Zh: in Vector_H;
                  D: in Vector;
                  Ch: in Vector_H;
                  MS: in Matrix);
     entry Send_a1_From_T1_To_T2(a1: out integer);
  end T1;

  task T2 is
     pragma Storage_Size(2_147_483_648);
     entry Send_Data_From_T1_To_T2(MX7h: in Matrix_7H);
     entry Send_Data_From_T3_To_T2(Z2h: in Vector_2H;
                  D: in Vector;
                  C2h: in Vector_2H;
                  MS: in Matrix);
     entry Send_a2_From_T2_To_T3(a2: out integer);
     entry Send_a_From_T2_To_T1(a: out integer);
     entry Send_A7h_From_T2_To_T1(A7h: out Vector_7H);
  end T2;

  task T3 is
     pragma Storage_Size(2_147_483_648);
     entry Send_Data_From_T2_To_T3(MX6h: in Matrix_6H);
     entry Send_Data_From_T4_To_T3(C3h: in Vector_3H;
                  MS: in Matrix);
     entry Send_a3_From_T3_To_T4(a3: out integer);
     entry Send_a_From_T3_To_T2(a: out integer);
     entry Send_A6h_From_T3_To_T2(A6h: out Vector_6H);
  end T3;

  task T4 is
     pragma Storage_Size(2_147_483_648);
     entry Send_Data_From_T3_To_T4(MX5h: in Matrix_5H;
                   Z5h: in Vector_5H;
                   D: in Vector);
     entry Send_a4_From_T4_To_T5(a4: out integer);
     entry Send_a_From_T4_To_T3(a: out integer);
     entry Send_A5h_From_T4_To_T3(A5h: out Vector_5H);
   end T4;

  task T5 is
     pragma Storage_Size(2_147_483_648);
     entry Send_Data_From_T4_To_T5(MX4h: in Matrix_4H;
                   Z4h: in Vector_4H;
                   D: in Vector;
                   C4h: in Vector_4H;
                   MS: in Matrix);
     entry Send_a5_From_T5_To_T6(a5: out integer);
     entry Send_a_From_T5_To_T4(a: out integer);
     entry Send_A4h_From_T5_To_T4(A4h: out Vector_4H);
  end T5;

  task T6 is
     pragma Storage_Size(2_147_483_648);
     entry Send_Data_From_T5_To_T6(MX3h: in Matrix_3H;
                   Z3h: in Vector_3H;
                   D: in Vector;
                   C3h: in Vector_3H;
                   MS: in Matrix);
     entry Send_a6_From_T6_To_T7(a6: out integer);
     entry Send_a_From_T6_To_T5(a: out integer);
     entry Send_A3h_From_T6_To_T5(A3h: out Vector_3H);
   end T6;

  task T7 is
     pragma Storage_Size(2_147_483_648);
     entry Send_Data_From_T6_To_T7(MX2h: in Matrix_2H;
                   Z2h: in Vector_2H;
                   D: in Vector;
                   C2h: in Vector_2H;
                   MS: in Matrix);
     entry Send_a7_From_T7_To_T8(a7: out integer);
     entry Send_a_From_T7_To_T6(a: out integer);
     entry Send_A2h_From_T7_To_T6(A2h: out Vector_2H);
   end T7;

  task T8 is
     pragma Storage_Size(2_147_483_648);
     entry Send_Data_From_T7_To_T8(MXh: in Matrix_H;
                   Zh: in Vector_H;
                   D: in Vector;
                   Ch: in Vector_H;
                   MS: in Matrix);
     entry Send_a_From_T8_To_T7(a: out integer);
     entry Send_Ah_From_T8_To_T7(Ah: out Vector_H);
   end T8;

  -- Опис тіл задач
  task body T1 is
     MX_T1, MS_T1: Matrix;
     MXhMS_T1: Matrix_H;
     D_T1, A_T1: Vector;
     Zh_T1, Ch_T1, DMXhMS_T1: Vector_H;
     aInt_T1: integer;
  begin
     put_line("Task T1 started");

     -- Введення MX
     for i in 1..N loop
        for j in 1..N loop
           MX_T1(i)(j) := 1;
        end loop;
     end loop;

     -- Передати задачі Т2 дані: MX7н
     T2.Send_Data_From_T1_To_T2(MX_T1(H+1..N));

     -- Прийняти від задачі Т2 дані:  Zн, D, Cн, MS
     accept Send_Data_From_T2_To_T1 (Zh : in Vector_H;
                    D : in Vector;
                    Ch : in Vector_H;
                    MS : in Matrix) do
        MS_T1:=MS;
        D_T1:=D;
        Ch_T1:=Ch;
        Zh_T1:=Zh;
     end Send_Data_From_T2_To_T1;

     -- Обчислення 1: a1 = max(Cн)
      aInt_T1:=0;
     for i in 1..H loop
         if Ch_T1(i) > aInt_T1 then
            aInt_T1:=Ch_T1(i);
         end if;
     end loop;

     -- Передати в задачу Т2 результат а1
     accept Send_a1_From_T1_To_T2 (a1 : out integer) do
        a1:=aInt_T1;
     end Send_a1_From_T1_To_T2;

     -- Прийняти від Т2 результат - скаляр а.
     T2.Send_a_From_T2_To_T1(aInt_T1);

     -- MXh*MS
     for i in 1..H loop
        for j in 1..N loop
           MXhMS_T1(i)(j):= 0;
           for k in 1..N loop
              MXhMS_T1(i)(j) := MXhMS_T1(i)(j) + MX_T1(i)(k) * MS_T1(k)(j);
           end loop;
        end loop;
     end loop;

     -- D * (MXh*MS)
     for i in 1..H loop
        DMXhMS_T1(i):=0;
        for k in 1..N loop
           DMXhMS_T1(i):= DMXhMS_T1(i) + MXhMS_T1(i)(k) * D_T1(k);
        end loop;
     end loop;

     -- Обчислення 2: Aн = a * Zн + D*(MXн*MS)
     for i in 1..H loop
        A_T1(i):= DMXhMS_T1(i) + (Zh_T1(i) * aInt_T1);
     end loop;

     -- Прийняти від Т2 результат - вектор A7н
     T2.Send_A7h_From_T2_To_T1(A_T1(H+1..N));

     -- Виведення результату – вектор A
     Put("A = ");
     for i in 1..N loop
        Put(A_T1(i), 1);
        Put(" ");
     end loop;
     New_Line;

     put_line("Task T1 finished");
  end T1;

  task body T2 is
     MS_T2: Matrix;
     MXhMS_T2: Matrix_H;
     MX7h_T2: Matrix_7H;
     D_T2: Vector;
     DMXhMS_T2: Vector_H;
     Z2h_T2, C2h_T2: Vector_2H;
     A7h_T2: Vector_7H;
     a_T2, a1_T2: integer;
  begin
     put_line("Task T2 started");

     -- Прийняти від задачі Т1 дані: MX7н
     accept Send_Data_From_T1_To_T2(MX7h : Matrix_7H) do
        MX7h_T2:= MX7h;
     end Send_Data_From_T1_To_T2;

     -- Передати задачі Т3 дані: MX6н
     T3.Send_Data_From_T2_To_T3(MX7h_T2(H+1..H*7));

     -- Прийняти від задачі Т3 дані: Z2н, D, C2н, MS
     accept Send_Data_From_T3_To_T2(Z2h: in Vector_2H;
                  D: in Vector;
                  C2h: in Vector_2H;
                  MS: in Matrix) do
        MS_T2:=MS;
        Z2h_T2:=Z2h;
        C2h_T2:=C2h;
        D_T2:=D;
     end Send_Data_From_T3_To_T2;

     -- Передати задачі Т1 дані: Zн, D, Cн, MS
     T1.Send_Data_From_T2_To_T1(Z2h_T2(1..H), D_T2, C2h_T2(1..H), MS_T2);

     -- Обчислення 1: a2 = max(Cн)
     a_T2:=0;
     for i in 1..H loop
         if C2h_T2(i) > a_T2 then
            a_T2:=C2h_T2(i);
         end if;
     end loop;

     -- Прийняти від Т1 результат - скаляр а. (а1)
      T1.Send_a1_From_T1_To_T2(a1_T2);

     -- Обчислення 2: a = max(а2, а1)
      if(a_T2 < a1_T2) then
        a_T2:= a1_T2;
      end if;

     -- Передати в задачу Т3 результат а
     accept Send_a2_From_T2_To_T3 (a2 : out integer) do
        a2:=a_T2;
     end Send_a2_From_T2_To_T3;

     -- Прийняти від Т3 результат - скаляр а
     T3.Send_a_From_T3_To_T2(a_T2);

     -- Передати в задачу Т1 результат а
     accept Send_a_From_T2_To_T1 (a : out integer) do
        a:= a_T2;
     end Send_a_From_T2_To_T1;

      -- MXh*MS
     for i in 1..H loop
        for j in 1..N loop
           MXhMS_T2(i)(j):= 0;
           for k in 1..N loop
              MXhMS_T2(i)(j) := MXhMS_T2(i)(j) + MX7h_T2(i)(k) * MS_T2(k)(j);
           end loop;
        end loop;
     end loop;

     -- D * (MXh*MS)
     for i in 1..H loop
        DMXhMS_T2(i):=0;
        for k in 1..N loop
           DMXhMS_T2(i):= DMXhMS_T2(i) + MXhMS_T2(i)(k) * D_T2(k);
        end loop;
     end loop;

     -- Обчислення 3: Aн = a * Zн + D*(MXн*MS)
     for i in 1..H loop
        A7h_T2(i):= DMXhMS_T2(i) + (Z2h_T2(i) * a_T2);
      end loop;

     -- Прийняти від Т3 результат - вектор A6н
     T3.Send_A6h_From_T3_To_T2(A7h_T2(H+1..H*7));

     -- Передати в задачу Т1 частину результату A7н
     accept Send_A7h_From_T2_To_T1 (A7h : out Vector_7H) do
        A7h:= A7h_T2;
     end Send_A7h_From_T2_To_T1;

     put_line("Task T2 finished");
   end T2;

  task body T3 is
     MS_T3: Matrix;
     MXhMS_T3: Matrix_H;
     MX6h_T3: Matrix_6H;
     D_T3, Z_T3: Vector;
     DMXhMS_T3: Vector_H;
     C3h_T3: Vector_3H;
     A6h_T3: Vector_6H;
     a_T3, a2_T3: integer;
  begin
     put_line("Task T3 started");

     -- Введення Z, D
     for i in 1..N loop
        Z_T3(i) := 1;
        D_T3(i) := 1;
     end loop;

     -- Прийняти від задачі Т2 дані: MX6н
     accept Send_Data_From_T2_To_T3 (MX6h : in Matrix_6H) do
        MX6h_T3 := MX6h;
     end Send_Data_From_T2_To_T3;

     -- Передати задачі Т4 дані: MX5н, Z5н, D
     T4.Send_Data_From_T3_To_T4(MX6h_T3(H+1..H*6), Z_T3(H*3+1..N), D_T3);

     -- Прийняти від задачі Т4 дані: C3н, MS
     accept Send_Data_From_T4_To_T3(C3h: in Vector_3H;
                  MS: in Matrix) do
        C3h_T3:=C3h;
        MS_T3:= MS;
     end Send_Data_From_T4_To_T3;

     -- Передати задачі Т2 дані: Z2н, D, C2н, MS
     T2.Send_Data_From_T3_To_T2(Z_T3(1..H*2), D_T3, C3h_T3(1..H*2), MS_T3);

     -- Обчислення 1: a3 = max(Cн)
     a_T3:=0;
     for i in 1..H loop
         if C3h_T3(i) > a_T3 then
            a_T3:=C3h_T3(i);
         end if;
     end loop;

     -- Прийняти від Т2 результат - скаляр а (a2)
     T2.Send_a2_From_T2_To_T3(a2_T3);

      -- Обчислення 2: а = max(а3, а2)
      if(a_T3 < a2_T3) then
         a_T3:= a2_T3;
      end if;

     -- Передати в задачу Т4 результат а
     accept Send_a3_From_T3_To_T4 (a3 : out integer) do
        a3:=a_T3;
     end Send_a3_From_T3_To_T4;

     -- Прийняти від Т4 результат - скаляр а
     T4.Send_a_From_T4_To_T3(a_T3);

     -- Передати в задачу Т2 результат а
     accept Send_a_From_T3_To_T2 (a : out integer) do
        a:= a_T3;
     end Send_a_From_T3_To_T2;

     -- MXh*MS
     for i in 1..H loop
        for j in 1..N loop
           MXhMS_T3(i)(j):= 0;
           for k in 1..N loop
              MXhMS_T3(i)(j) := MXhMS_T3(i)(j) + MX6h_T3(i)(k) * MS_T3(k)(j);
           end loop;
        end loop;
     end loop;

     -- D * (MXh*MS)
     for i in 1..H loop
        DMXhMS_T3(i):=0;
        for k in 1..N loop
           DMXhMS_T3(i):= DMXhMS_T3(i) + MXhMS_T3(i)(k) * D_T3(k);
        end loop;
     end loop;

     -- Обчислення 3: Aн = a * Zн + D*(MXн*MS)
     for i in 1..H loop
        A6h_T3(i):= DMXhMS_T3(i) + (Z_T3(i) * a_T3);
      end loop;

     -- Прийняти від Т4 результат - вектор A5н
     T4.Send_A5h_From_T4_To_T3(A6h_T3(H+1..H*6));

     -- Передати в задачу Т2 частину результату A6н
     accept Send_A6h_From_T3_To_T2 (A6h : out Vector_6H) do
        A6h:= A6h_T3;
     end Send_A6h_From_T3_To_T2;

     put_line("Task T3 finished");
  end T3;

  task body T4 is
     MS_T4: Matrix;
     MXhMS_T4: Matrix_H;
     MX5h_T4: Matrix_5H;
     D_T4, C_T4: Vector;
     DMXhMS_T4: Vector_H;
     Z5h_T4, A5h_T4: Vector_5H;
     a_T4, a3_T4: integer;
  begin
     put_line("Task T4 started");

     -- Введення C, MS
     for i in 1..N loop
        for j in 1..N loop
           MS_T4(i)(j) := 1;
        end loop;
        C_T4(i) := 1;
     end loop;

     -- Прийняти від задачі Т3 дані: MX5н, Z5н, D
     accept Send_Data_From_T3_To_T4(MX5h: in Matrix_5H;
                   Z5h: in Vector_5H;
                   D: in Vector) do
        MX5h_T4:=MX5h;
        Z5h_T4:=Z5h;
        D_T4:=D;
     end Send_Data_From_T3_To_T4;

     -- Передати задачі Т5 дані: MX4н, Z4н, D, C4н, MS
     T5.Send_Data_From_T4_To_T5(MX5h_T4(H+1..H*5), Z5h_T4(H+1..H*5), D_T4, C_T4(H*4+1..N), MS_T4);

     -- Передати задачі Т3 дані: C3н, MS
     T3.Send_Data_From_T4_To_T3(C_T4(1..H*3), MS_T4);

     -- Обчислення 1: a4 = max(Cн)
     a_T4:=0;
     for i in 1..H loop
         if C_T4(i) > a_T4 then
            a_T4:=C_T4(i);
         end if;
     end loop;

     -- Прийняти від Т3 результат - скаляр а (a3)
     T3.Send_a3_From_T3_To_T4(a3_T4);

     -- Обчислення 2: а = max(а4, а3)
      if(a_T4 < a3_T4) then
         a_T4:= a3_T4;
      end if;

     -- Передати в задачу Т5 результат а
     accept Send_a4_From_T4_To_T5 (a4 : out integer) do
        a4:=a_T4;
     end Send_a4_From_T4_To_T5;

     -- Прийняти від Т5 результат - скаляр а
     T5.Send_a_From_T5_To_T4(a_T4);

     -- Передати в задачу Т3 результат а
     accept Send_a_From_T4_To_T3 (a : out integer) do
        a:= a_T4;
     end Send_a_From_T4_To_T3;

     -- MXh*MS
     for i in 1..H loop
        for j in 1..N loop
           MXhMS_T4(i)(j):= 0;
           for k in 1..N loop
              MXhMS_T4(i)(j) := MXhMS_T4(i)(j) + MX5h_T4(i)(k) * MS_T4(k)(j);
           end loop;
        end loop;
     end loop;

     -- D * (MXh*MS)
     for i in 1..H loop
        DMXhMS_T4(i):=0;
        for k in 1..N loop
           DMXhMS_T4(i):= DMXhMS_T4(i) + MXhMS_T4(i)(k) * D_T4(k);
        end loop;
     end loop;

     -- Обчислення 3: Aн = a * Zн + D*(MXн*MS)
     for i in 1..H loop
        A5h_T4(i):= DMXhMS_T4(i) + (Z5h_T4(i) * a_T4);
      end loop;

     -- Прийняти від Т5 результат - вектор A4н
     T5.Send_A4h_From_T5_To_T4(A5h_T4(H+1..H*5));

     -- Передати в задачу Т3 частину результату A5н
     accept Send_A5h_From_T4_To_T3 (A5h : out Vector_5H) do
        A5h:= A5h_T4;
     end Send_A5h_From_T4_To_T3;

     put_line("Task T4 finished");
  end T4;

  task body T5 is
     MS_T5: Matrix;
     MXhMS_T5: Matrix_H;
     MX4h_T5: Matrix_4H;
     D_T5: Vector;
     DMXhMS_T5: Vector_H;
     Z4h_T5, A4h_T5, C4h_T5: Vector_4H;
     a_T5, a4_T5: integer;
  begin
      put_line("Task T5 started");

     -- Прийняти від задачі Т4 дані: MX4н, Z4н, D, C4н, MS
     accept Send_Data_From_T4_To_T5(MX4h: in Matrix_4H;
                   Z4h: in Vector_4H;
                   D: in Vector;
                   C4h: in Vector_4H;
                   MS: in Matrix) do
        MX4h_T5:=MX4h;
        Z4h_T5:=Z4h;
        D_T5:=D;
        C4h_T5:=C4h;
        MS_T5:=MS;
      end Send_Data_From_T4_To_T5;

      -- Передати задачі Т6 дані: MX3н, Z3н, D, C3н, MS
      T6.Send_Data_From_T5_To_T6(MX4h_T5(H+1..H*4), Z4h_T5(H+1..H*4), D_T5, C4h_T5(H+1..H*4), MS_T5);

     -- Обчислення 1: a5= max(Cн)
     a_T5:=0;
     for i in 1..H loop
         if C4h_T5(i) > a_T5 then
            a_T5:=C4h_T5(i);
         end if;
     end loop;

     -- Прийняти від Т4 результат - скаляр а (a4)
     T4.Send_a4_From_T4_To_T5(a4_T5);

     -- Обчислення 2: а = max(а5, а4)
      if(a_T5 < a4_T5) then
         a_T5:= a4_T5;
      end if;

     -- Передати в задачу Т6 результат а
     accept Send_a5_From_T5_To_T6 (a5 : out integer) do
        a5:=a_T5;
     end Send_a5_From_T5_To_T6;

     -- Прийняти від Т6 результат - скаляр а
     T6.Send_a_From_T6_To_T5(a_T5);

     -- Передати в задачу Т4 результат а
     accept Send_a_From_T5_To_T4 (a : out integer) do
        a:= a_T5;
     end Send_a_From_T5_To_T4;

     -- MXh*MS
     for i in 1..H loop
        for j in 1..N loop
           MXhMS_T5(i)(j):= 0;
           for k in 1..N loop
              MXhMS_T5(i)(j) := MXhMS_T5(i)(j) + MX4h_T5(i)(k) * MS_T5(k)(j);
           end loop;
        end loop;
     end loop;

     -- D * (MXh*MS)
     for i in 1..H loop
        DMXhMS_T5(i):=0;
        for k in 1..N loop
           DMXhMS_T5(i):= DMXhMS_T5(i) + MXhMS_T5(i)(k) * D_T5(k);
        end loop;
     end loop;

     -- Обчислення 3: Aн = a * Zн + D*(MXн*MS)
     for i in 1..H loop
        A4h_T5(i):= DMXhMS_T5(i) + (Z4h_T5(i) * a_T5);
      end loop;

     -- Прийняти від Т6 результат - вектор A3н
     T6.Send_A3h_From_T6_To_T5(A4h_T5(H+1..H*4));

     -- Передати в задачу Т4 частину результату A4н
     accept Send_A4h_From_T5_To_T4 (A4h : out Vector_4H) do
        A4h:= A4h_T5;
     end Send_A4h_From_T5_To_T4;

     put_line("Task T5 finished");
  end T5;

  task body T6 is
     MS_T6: Matrix;
     MXhMS_T6: Matrix_H;
     MX3h_T6: Matrix_3H;
     D_T6: Vector;
     DMXhMS_T6: Vector_H;
     Z3h_T6, A3h_T6, C3h_T6: Vector_3H;
     a_T6, a5_T6: integer;
  begin
      put_line("Task T6 started");

     -- Прийняти від задачі Т5 дані: MX3н, Z3н, D, C3н, MS
     accept Send_Data_From_T5_To_T6(MX3h: in Matrix_3H;
                   Z3h: in Vector_3H;
                   D: in Vector;
                   C3h: in Vector_3H;
                   MS: in Matrix) do
        MX3h_T6:=MX3h;
        Z3h_T6:=Z3h;
        D_T6:=D;
        C3h_T6:=C3h;
        MS_T6:=MS;
      end Send_Data_From_T5_To_T6;

      -- Передати задачі Т7 дані: MX2н, Z2н, D, C2н, MS
      T7.Send_Data_From_T6_To_T7(MX3h_T6(H+1..H*3), Z3h_T6(H+1..H*3), D_T6, C3h_T6(H+1..H*3), MS_T6);

     -- Обчислення 1: a6= max(Cн)
     a_T6:=0;
     for i in 1..H loop
         if C3h_T6(i) > a_T6 then
            a_T6:=C3h_T6(i);
         end if;
     end loop;

     -- Прийняти від Т5 результат - скаляр а (a5)
     T5.Send_a5_From_T5_To_T6(a5_T6);

     -- Обчислення 2: а = max(а6, а5)
      if(a_T6 < a5_T6) then
         a_T6:= a5_T6;
      end if;

     -- Передати в задачу Т7 результат а
     accept Send_a6_From_T6_To_T7 (a6 : out integer) do
        a6:=a_T6;
     end Send_a6_From_T6_To_T7;

     -- Прийняти від Т7 результат - скаляр а
     T7.Send_a_From_T7_To_T6(a_T6);

     -- Передати в задачу Т5 результат а
     accept Send_a_From_T6_To_T5 (a : out integer) do
        a:= a_T6;
     end Send_a_From_T6_To_T5;

     -- MXh*MS
     for i in 1..H loop
        for j in 1..N loop
           MXhMS_T6(i)(j):= 0;
           for k in 1..N loop
              MXhMS_T6(i)(j) := MXhMS_T6(i)(j) + MX3h_T6(i)(k) * MS_T6(k)(j);
           end loop;
        end loop;
     end loop;

     -- D * (MXh*MS)
     for i in 1..H loop
        DMXhMS_T6(i):=0;
        for k in 1..N loop
           DMXhMS_T6(i):= DMXhMS_T6(i) + MXhMS_T6(i)(k) * D_T6(k);
        end loop;
     end loop;

     -- Обчислення 3: Aн = a * Zн + D*(MXн*MS)
     for i in 1..H loop
        A3h_T6(i):= DMXhMS_T6(i) + (Z3h_T6(i) * a_T6);
     end loop;

     -- Прийняти від Т7 результат - вектор A2н
     T7.Send_A2h_From_T7_To_T6(A3h_T6(H+1..H*3));

     -- Передати в задачу Т5 частину результату A3н
     accept Send_A3h_From_T6_To_T5 (A3h : out Vector_3H) do
        A3h:= A3h_T6;
     end Send_A3h_From_T6_To_T5;

     put_line("Task T6 finished");
  end T6;

  task body T7 is
     MS_T7: Matrix;
     MXhMS_T7: Matrix_H;
     MX2h_T7: Matrix_2H;
     D_T7: Vector;
     DMXhMS_T7: Vector_H;
     Z2h_T7, C2h_T7, A2h_T7: Vector_2H;
     a_T7, a6_T7: integer;
  begin
     put_line("Task T7 started");

     -- Прийняти від задачі Т6 дані: MX2н, Z2н, D, C2н, MS
     accept Send_Data_From_T6_To_T7(MX2h: in Matrix_2H;
                   Z2h: in Vector_2H;
                   D: in Vector;
                   C2h: in Vector_2H;
                   MS: in Matrix) do
        MX2h_T7:=MX2h;
        Z2h_T7:=Z2h;
        D_T7:=D;
        C2h_T7:=C2h;
        MS_T7:=MS;
      end Send_Data_From_T6_To_T7;

      -- Передати задачі Т8 дані: MXн, Zн, D, Cн, MS
      T8.Send_Data_From_T7_To_T8(MX2h_T7(H+1..H*2), Z2h_T7(H+1..H*2), D_T7, C2h_T7(H+1..H*2), MS_T7);

     -- Обчислення 1: a7= max(Cн)
     a_T7:=0;
     for i in 1..H loop
         if C2h_T7(i) > a_T7 then
            a_T7:=C2h_T7(i);
         end if;
     end loop;

     -- Прийняти від Т6 результат - скаляр а (a6)
     T6.Send_a6_From_T6_To_T7(a6_T7);


     -- Обчислення 2: а = max(а7, а6)
      if(a_T7 < a6_T7) then
         a_T7:= a6_T7;
      end if;

     -- Передати в задачу Т8 результат а
     accept Send_a7_From_T7_To_T8 (a7 : out integer) do
        a7:=a_T7;
     end Send_a7_From_T7_To_T8;

     -- Прийняти від Т8 результат - скаляр а
     T8.Send_a_From_T8_To_T7(a_T7);

     -- Передати в задачу Т6 результат а
     accept Send_a_From_T7_To_T6 (a : out integer) do
        a:= a_T7;
     end Send_a_From_T7_To_T6;

     -- MXh*MS
     for i in 1..H loop
        for j in 1..N loop
           MXhMS_T7(i)(j):= 0;
           for k in 1..N loop
              MXhMS_T7(i)(j) := MXhMS_T7(i)(j) + MX2h_T7(i)(k) * MS_T7(k)(j);
           end loop;
        end loop;
     end loop;

     -- D * (MXh*MS)
     for i in 1..H loop
        DMXhMS_T7(i):=0;
        for k in 1..N loop
           DMXhMS_T7(i):= DMXhMS_T7(i) + MXhMS_T7(i)(k) * D_T7(k);
        end loop;
     end loop;

     -- Обчислення 3: Aн = a * Zн + D*(MXн*MS)
     for i in 1..H loop
        A2h_T7(i):= DMXhMS_T7(i) + (Z2h_T7(i) * a_T7);
     end loop;

     -- Прийняти від Т8 результат - вектор Aн
     T8.Send_Ah_From_T8_To_T7(A2h_T7(H+1..H*2));

     -- Передати в задачу Т6 частину результату A2н
     accept Send_A2h_From_T7_To_T6 (A2h : out Vector_2H) do
        A2h:= A2h_T7;
     end Send_A2h_From_T7_To_T6;

     put_line("Task T7 finished");
  end T7;

  task body T8 is
     MS_T8: Matrix;
     MXhMS_T8, MXh_T8: Matrix_H;
     D_T8: Vector;
     DMXhMS_T8, Zh_T8, Ch_T8, Ah_T8: Vector_H;
     a_T8, a7_T8: integer;
  begin
     put_line("Task T8 started");

     -- Прийняти від задачі Т7 дані: MXн, Zн, D, Cн, MS
     accept Send_Data_From_T7_To_T8(MXh: in Matrix_H;
                   Zh: in Vector_H;
                   D: in Vector;
                   Ch: in Vector_H;
                   MS: in Matrix) do
        MXh_T8:=MXh;
        Zh_T8:=Zh;
        D_T8:=D;
        Ch_T8:=Ch;
        MS_T8:=MS;
      end Send_Data_From_T7_To_T8;

     -- Обчислення 1: a8= max(Cн)
     a_T8:=0;
     for i in 1..H loop
         if Ch_T8(i) > a_T8 then
            a_T8:=Ch_T8(i);
         end if;
     end loop;

     -- Прийняти від Т7 результат - скаляр а (a7)
     T7.Send_a7_From_T7_To_T8(a7_T8);

     -- Обчислення 2: а = max(а8, а7)
      if(a_T8 < a7_T8) then
         a_T8:= a7_T8;
      end if;

     -- Передати в задачу Т7 результат а
     accept Send_a_From_T8_To_T7 (a : out integer) do
        a:= a_T8;
     end Send_a_From_T8_To_T7;

     -- MXh*MS
     for i in 1..H loop
        for j in 1..N loop
           MXhMS_T8(i)(j):= 0;
           for k in 1..N loop
              MXhMS_T8(i)(j) := MXhMS_T8(i)(j) + MXh_T8(i)(k) * MS_T8(k)(j);
           end loop;
        end loop;
     end loop;

     -- D * (MXh*MS)
     for i in 1..H loop
        DMXhMS_T8(i):=0;
        for k in 1..N loop
           DMXhMS_T8(i):= DMXhMS_T8(i) + MXhMS_T8(i)(k) * D_T8(k);
        end loop;
     end loop;

     -- Обчислення 3: Aн = a * Zн + D*(MXн*MS)
     for i in 1..H loop
        Ah_T8(i):= DMXhMS_T8(i) + (Zh_T8(i) * a_T8);
     end loop;

     -- Передати в задачу Т7 частину результату Aн
     accept Send_Ah_From_T8_To_T7 (Ah : out Vector_H) do
        Ah:= Ah_T8;
     end Send_Ah_From_T8_To_T7;

     put_line("Task T8 finished");
  end T8;

begin
  null;
end Lab5;